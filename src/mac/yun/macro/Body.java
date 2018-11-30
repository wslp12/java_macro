package mac.yun.macro;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent.EventType;

import mac.yun.model.ExeList;
import mac.yun.model.ListStr;
import mac.yun.model.MacroEvnetType;

public class Body extends JFrame{
	private JButton addBtn; // add
	private JRadioButton insertBtn; // insertion
	private JButton removeBtn; // remove
	
	private JPanel rightJpnel;
	private JPanel rightBottomPane;
	private JPanel leftPane;
	private String [] fruits= {"추가", "삽입", "반복"};
	private String [] eventString = {ListStr.MOVEMOSE, ListStr.PRESSMOUSE,ListStr.UNPRESSMOUSE ,ListStr.CLICKMOUSE ,ListStr.TIME ,ListStr.CLICKKEYBOARD};
	private Vector<ListStr> addValues = new Vector<ListStr>();
	private JList<String> scrollList;
	
	public JLabel jb;
	public JLabel jb2;
	
	private int rootx = 600;
	private int rooty = 300;
	
	private Boolean interub = false;
	public JDialog dialog;
	
	Robot robot;
	
	public Body() {
		super.setTitle("윤지만매크로");
		super.setBounds(rootx, rooty, 450, 360);
		super.setLayout(null);
		super.setResizable(false);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			robot = new Robot();
		}
		catch (AWTException e) {
			e.printStackTrace();
		}
		this.rightPanelInit();
		this.rightBottomPanelInit();
		this.leftPanInit();
		this.startButonInit();
		this.removeButonInit(); 

		this.scrollList = new JList<String>(); 
		this.leftPane.add(new JScrollPane(scrollList)); 

		super.setVisible(true);
	}
	
	private void rightPanelInit() {
		this.rightJpnel = new JPanel();
		this.rightJpnel.setLayout(new BorderLayout());
		this.rightJpnel.setBackground(Color.BLACK);
		this.rightJpnel.setBounds(220, 10, 200, 60);
		this.rightJpnel.setVisible(true);
		JList<String> strList = new JList<String>(fruits);
		strList.setSelectedIndex(0);
        this.rightJpnel.add(strList);
        this.add(this.rightJpnel);
	}
	
	private void rightBottomPanelInit() {
		this.rightBottomPane = new JPanel();
		this.rightBottomPane.setLayout(new BorderLayout());
		this.rightBottomPane.setBackground(Color.BLACK);
		this.rightBottomPane.setBounds(220, 130, 200, 178);
		this.rightBottomPane.setVisible(true);
		
		JList<String> strList = new JList<String>(eventString);
        this.rightBottomPane.add(strList);
        this.add(this.rightBottomPane);
        
        strList.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (strList.getSelectedIndex() == 0 ) { // 마우스 이동
					System.out.println(strList.getSelectedValue());
					dialog = new JDialog();
					
					dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					dialog.setLayout(null);
					dialog.setTitle("마우스 이동");
					dialog.setModal(true);
					dialog.setResizable(false);
					dialog.setBounds(rootx+50, rooty+50, 150, 180); 
					
					JPanel jp = new JPanel();
					JPanel jp2 = new JPanel();
					JPanel jp3 = new JPanel();
					jp.setLayout(null);
					jp2.setLayout(null);
					jp3.setLayout(null);
					
					jb = new JLabel("X:");
					jb.setBounds(0, 0, 50, 50);
					jb2 = new JLabel("Y:");
					jb2.setBounds(0, 0, 50, 50);
					
					JTextField xjf = new JTextField();
					xjf.setBounds(80, 10, 50, 25);
					JTextField yjf = new JTextField();
					yjf.setBounds(80, 10, 50, 25);
					
					JButton jbtn = new JButton("적용");
					jbtn.setBounds(0, 0, 150, 50);
					
					jp.add(jb);
					jp.add(xjf);
					jp2.add(jb2);
					jp2.add(yjf);
					jp3.add(jbtn);
					
					jp.setBackground(Color.GREEN);
					jp.setBounds(0, 0, 350, 50);
					jp2.setBackground(Color.YELLOW);
					jp2.setBounds(0, 50, 350, 50);
					jp3.setBackground(Color.BLUE);
					jp3.setBounds(0, 100, 350, 50);
					dialog.add(jp);
					dialog.add(jp2);
					dialog.add(jp3);
					
					
					jbtn.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								interub = true; // stop Thread
								dialog.dispose();
								ListStr e1 = new ExeList();
								e1.type = strList.getSelectedValue();
								e1.text = strList.getSelectedValue() + "("+xjf.getText() +","+ yjf.getText()+")".toString();
								e1.x = Integer.parseInt(xjf.getText());
								e1.y = Integer.parseInt(yjf.getText());
								addValues.add(e1);
								
								String [] strArr = new String[addValues.size()];
								for (int i = 0 ; i < strArr.length; i ++) {
									strArr[i] = addValues.get(i).text;
								}
								scrollList.setListData(strArr);
								
							} catch (Exception e2) {
								System.out.println(e2);
							}
						}
					});
					
					MyThread myThread = new MyThread();
					dialog.addWindowListener(new WindowListener() {
						@Override
						public void windowOpened(WindowEvent e) {
							interub = false;
							myThread.start();
						}
						
						@Override
						public void windowIconified(WindowEvent e) {}
						
						@Override
						public void windowDeiconified(WindowEvent e) {}
						
						@Override
						public void windowDeactivated(WindowEvent e) {}
						
						@Override
						public void windowClosing(WindowEvent e) {
							System.out.println("closeWIndow");
							try {
								interub = true;
							} catch ( Exception e2) {}
						}
						
						@Override
						public void windowClosed(WindowEvent e) {}
						
						@Override
						public void windowActivated(WindowEvent e) {}
					});
					dialog.setVisible( true );
				} else if (strList.getSelectedIndex() == 1 ) { // 마우스 누름
					System.out.println(strList.getSelectedValue());
					ListStr exeList = new ExeList();
					exeList.type = strList.getSelectedValue();
					exeList.text = strList.getSelectedValue() + "(누름)";
					exeList.mouseButton = 1;
					addValues.add(exeList);
					String [] strArr = new String[addValues.size()];
					for (int i = 0 ; i < strArr.length; i ++) {
						strArr[i] = addValues.get(i).text;
					}
					scrollList.setListData(strArr);
				} else if (strList.getSelectedIndex() == 2 ) { // 마우스 해제
					System.out.println(strList.getSelectedValue());
					ListStr exeList = new ExeList();
					exeList.type = strList.getSelectedValue();
					exeList.text = strList.getSelectedValue() + "(해제)";
					exeList.mouseButton = 1;
					addValues.add(exeList);
					String [] strArr = new String[addValues.size()];
					for (int i = 0 ; i < strArr.length; i ++) {
						strArr[i] = addValues.get(i).text;
					}
					scrollList.setListData(strArr);
				} else if (strList.getSelectedIndex() == 3 ) { // 마우스 클릭
					System.out.println(strList.getSelectedValue());
					ListStr exeList = new ExeList();
					exeList.type = strList.getSelectedValue();
					exeList.text = strList.getSelectedValue() + "(클릭)";
					exeList.mouseButton = 1;
					addValues.add(exeList);
					String [] strArr = new String[addValues.size()];
					for (int i = 0 ; i < strArr.length; i ++) {
						strArr[i] = addValues.get(i).text;
					}
					scrollList.setListData(strArr);
				} else if (strList.getSelectedIndex() == 4 ) { // 시간
					System.out.println(strList.getSelectedValue());
					dialog = new JDialog();
					dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					dialog.setLayout(null);
					dialog.setTitle("시간초 입력 ms");
					dialog.setModal(true);
					dialog.setResizable(false);
					dialog.setBounds(rootx+50, rooty+50, 100, 90); // FIXME: 상대 좌표로 수정
					
					JPanel jp = new JPanel();
					
					
					jb = new JLabel("ms");
					jp.setLayout(null);
					jb.setBounds(70, 0, 50, 20);
					
					JTextField timeInput = new JTextField();
					timeInput.setBounds(0, 0, 70, 25);
					
					JButton jbtn = new JButton("적용");
					jbtn.setBounds(0, 25, 100, 30);
					jbtn.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								ListStr exeList = new ExeList();
								exeList.type = strList.getSelectedValue();
								exeList.text = strList.getSelectedValue() + "(" + timeInput.getText() + ".ms)";
								exeList.time = Integer.parseInt(timeInput.getText());
								addValues.add(exeList);
								String [] strArr = new String[addValues.size()];
								for (int i = 0 ; i < strArr.length; i ++) {
									strArr[i] = addValues.get(i).text;
								}
								scrollList.setListData(strArr);
								dialog.dispose();
							} catch (Exception e2 ) {
								System.out.println(e2);
							}
						}
					});
					
					jp.add(jb);
					jp.add(timeInput);
					jp.add(jbtn);
					jp.setBounds(0, 0, 100, 100);
					dialog.add(jp);
					dialog.setVisible(true);
				} else if (strList.getSelectedIndex() == 5 ) { // 키보드 클릭
					System.out.println(strList.getSelectedValue());
					dialog = new JDialog();
					dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					dialog.setLayout(null);
					dialog.setTitle("키를 입력하시오");
					dialog.setModal(true);
					dialog.setResizable(false);
					dialog.setBounds(rootx+50, rooty+50, 220, 90); // FIXME: 상대좌표로 수정
					
					JPanel jp = new JPanel();
					jp.setLayout(null);
					
					jb = new JLabel("키를 입력하세요");
					jb.setBounds(10, 10, 400, 20);
					jp.add(jb);
					jp.setBounds(0, 0, 200, 100);
					dialog.addWindowListener(new WindowListener() {
						
						@Override
						public void windowOpened(WindowEvent e) {
							dialog.addKeyListener(new KeyListener() {
								@Override
								public void keyTyped(KeyEvent e) {}
								
								@Override
								public void keyReleased(KeyEvent e) {
									System.out.println(e.getKeyCode());
									ListStr exeList = new ExeList();
									exeList.type = strList.getSelectedValue();
									exeList.text = strList.getSelectedValue() + "(" + e.getKeyChar() + ")";
									exeList.keyCode = e.getKeyCode();
									addValues.add(exeList);
									String [] strArr = new String[addValues.size()];
									for (int i = 0 ; i < strArr.length; i ++) {
										strArr[i] = addValues.get(i).text;
									}
									scrollList.setListData(strArr);
									dialog.dispose();
								}
								
								@Override
								public void keyPressed(KeyEvent e) {
									// TODO Auto-generated method stub
									
								}
							});
						}
						
						@Override
						public void windowIconified(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void windowDeiconified(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void windowDeactivated(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void windowClosing(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void windowClosed(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void windowActivated(WindowEvent e) {
							// TODO Auto-generated method stub
							
						}
					});
					dialog.add(jp);
					dialog.setVisible(true);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void leftPanInit() {
		this.leftPane = new JPanel();
		this.leftPane.setLayout(new BorderLayout());
		this.leftPane.setBackground(Color.BLACK);
		this.leftPane.setBounds(10, 10, 200, 300);
		this.leftPane.setVisible(true);
		super.add(this.leftPane);
	}
	
	private void startButonInit() {
		this.addBtn = new JButton("실행");
        this.addBtn.setBounds(320, 80, 98, 40);
        this.add(addBtn);
        
        this.addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < addValues.size(); i ++) {
					switch (addValues.get(i).type) {
						case ListStr.MOVEMOSE :
							System.out.println(addValues.get(i).type);
							System.out.println(addValues.get(i).x);
							System.out.println(addValues.get(i).y);
							robot.mouseMove(addValues.get(i).x, addValues.get(i).y);
							break;
						case ListStr.TIME :
							try {
								Thread.sleep(addValues.get(i).time);
							} catch( Exception e2 ) {
								
							}
							break;
						case ListStr.CLICKMOUSE :
							robot.mousePress(InputEvent.BUTTON1_MASK);
							robot.mouseRelease(InputEvent.BUTTON1_MASK);
							break;
						case ListStr.PRESSMOUSE :
							robot.mousePress(InputEvent.BUTTON1_MASK);
							break;
						case ListStr.UNPRESSMOUSE:
							robot.mouseRelease(InputEvent.BUTTON1_MASK);
							break;
						case ListStr.CLICKKEYBOARD:
							robot.keyPress(addValues.get(i).keyCode);
							robot.keyRelease(addValues.get(i).keyCode);
							break;
					}
				}
				
			}
		});
	}
	
	private void removeButonInit() {
		this.removeBtn = new JButton("삭제");
		this.removeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(scrollList.getSelectedIndex());
				if (scrollList.getSelectedIndex() != -1) {
					addValues.remove(scrollList.getSelectedIndex());
					String [] strArr = new String[addValues.size()];
					for (int i = 0 ; i < strArr.length; i ++) {
						strArr[i] = addValues.get(i).text;
					}
					scrollList.setListData(strArr);
				}
				
			}
		});
        this.removeBtn.setBounds(220, 80, 98, 40);
        this.add(removeBtn);
	}
	
	public class MyThread extends Thread {
		public synchronized void run() {
			System.out.println("Thred Start");
			while ( true ) {
				try {
				PointerInfo inf = MouseInfo.getPointerInfo();
				Point p = inf.getLocation();
				jb.setText("x: " + p.getX());
				jb2.setText("x: " + p.getY());
				Thread.sleep(1000);
				} catch ( Exception e ) { 
					
				}
				if (interub) {
					System.out.println("Thred End");
					return;
				}
			}
		}
	}
}



