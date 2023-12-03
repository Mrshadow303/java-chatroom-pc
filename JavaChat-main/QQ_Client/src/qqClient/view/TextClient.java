package qqClient.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sip.MessageProcessor;
import sip.SipLayer;
/**
 * 
 * 
 * 	sip GUI
 * 		
 */
public class TextClient extends JFrame implements MessageProcessor{
    private SipLayer sipLayer;
    
    private JTextField fromAddress;
    private JLabel fromLbl;
    private JLabel receivedLbl;
    private JTextArea receivedMessages;
    private JScrollPane receivedScrollPane;
    private JButton sendBtn;
    private JLabel sendLbl;
    private JTextField sendMessages;
    private JTextField toAddress;
    private JLabel toLbl;
    private JButton sendfileBtn;
    private JButton choosefileBtn;

    

    public TextClient(SipLayer sip)
    {
        super();
        sipLayer = sip;
        initWindow();
        String from = "sip:" + sip.getUsername() + "@" + sip.getHost() + ":" + sip.getPort();
        this.fromAddress.setText(from);
    }
    
    private void initWindow() {
        receivedLbl = new JLabel();
        sendLbl = new JLabel();
        sendMessages = new JTextField();
        receivedScrollPane = new JScrollPane();
        receivedMessages = new JTextArea();
        fromLbl = new JLabel();
        fromAddress = new JTextField();
        toLbl = new JLabel();
        toAddress = new JTextField();
        sendBtn = new JButton();
        sendfileBtn = new JButton();
        choosefileBtn = new JButton();

        getContentPane().setLayout(null);

        setTitle(sipLayer.getUsername()+"的聊天窗口");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });

        receivedLbl.setText("消息面板:");
        receivedLbl.setFont(new Font("黑体", Font.PLAIN, 16));
        receivedLbl.setAlignmentY(0.0F);
        receivedLbl.setPreferredSize(new java.awt.Dimension(50, 200));
        getContentPane().add(receivedLbl);
        receivedLbl.setBounds(5, 5, 136, 20);

        sendLbl.setText("输入:");
        sendLbl.setFont(new Font("黑体", Font.PLAIN, 16));
        getContentPane().add(sendLbl);
        sendLbl.setBounds(5, 300, 90, 20);

        getContentPane().add(sendMessages);
        sendMessages.setBounds(5, 330, 546, 30);

        receivedMessages.setAlignmentX(0.0F);
        receivedMessages.setEditable(false);
        receivedMessages.setLineWrap(true);
        receivedMessages.setWrapStyleWord(true);
        receivedScrollPane.setViewportView(receivedMessages);
        receivedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(receivedScrollPane);
        receivedScrollPane.setBounds(5, 30, 546, 260);

        fromLbl.setText("发送方:");
        fromLbl.setFont(new Font("黑体", Font.PLAIN, 16));
        getContentPane().add(fromLbl);
        fromLbl.setBounds(5, 380, 70, 30);

        getContentPane().add(fromAddress);
        fromAddress.setBounds(80, 380, 470, 30);
        fromAddress.setEditable(false);

        toLbl.setText("接收方:");
        toLbl.setFont(new Font("黑体", Font.PLAIN, 16));
        getContentPane().add(toLbl);
        toLbl.setBounds(5, 430, 70, 30);

        getContentPane().add(toAddress);
        toAddress.setBounds(80, 430, 470, 30);

        //发送文件
        sendfileBtn.setText("Sendfile");
        sendfileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendfileBtnActionPerformed(evt);
            }
        });

        getContentPane().add(sendfileBtn);
        sendfileBtn.setBounds(160, 475, 140, 25);
      //选择文件
        choosefileBtn.setText("choosefile");
        choosefileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                choosefileBtnActionPerformed(evt);
            }
        });

        getContentPane().add(choosefileBtn);
        choosefileBtn.setBounds(5, 475, 140, 25);
        
        
        //发送消息
        sendBtn.setText("Send");
        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        getContentPane().add(sendBtn);
        sendBtn.setBounds(473, 475, 75, 25);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-576)/2, (screenSize.height-640)/2, 576, 550);
    }

    private void sendBtnActionPerformed(ActionEvent evt) {

        try
        {
            String to = this.toAddress.getText();
            String message = this.sendMessages.getText();
            sipLayer.sendMessage(to, message);
        } catch (Throwable e)
        {
            e.printStackTrace();
            this.receivedMessages.append("ERROR sending message: " + e.getMessage() + "\n");
        }
        			
    }
    
    private void sendfileBtnActionPerformed(ActionEvent evt) {

        try
        {
            String to = this.toAddress.getText();
            String message = this.sendMessages.getText();
            sipLayer.sendFile(to, message);
        } catch (Throwable e)
        {
            e.printStackTrace();
            this.receivedMessages.append("ERROR sending message: " + e.getMessage() + "\n");
        }
        			
    }
    private void choosefileBtnActionPerformed(ActionEvent evt) {

        try
        {
        	//文件选择器
            JFileChooser chooser = new JFileChooser();
            
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//文件名和文件
            
            /* JFileChooser 文件选择器
             * 参数: 
             *     parent: 文件选取器对话框的父组件, 对话框将会尽量显示在靠近 parent 的中心; 如果传 null, 则显示在屏幕中心。
             * 
             * 返回值:
             *     JFileChooser.CANCEL_OPTION: 点击了取消或关闭
             *     JFileChooser.APPROVE_OPTION: 点击了确认或保存
             *     JFileChooser.ERROR_OPTION: 出现错误
             */
            // 在获取用户选择的文件之前，通常先验证返回值是否为 APPROVE_OPTION.
            int num = chooser.showOpenDialog(null);
            
            // 若选择了文件，则打印选择的文件路径
            if(num == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();         // 获取文件
                sendMessages.setText(file.getAbsolutePath());  // 输出文件路径在txt_SendFile
            }
        } catch (Throwable e)
        {
            e.printStackTrace();
            this.receivedMessages.append("ERROR sending message: " + e.getMessage() + "\n");
        }
        			
    }

    public void processMessage(String sender, String message)
    {
        this.receivedMessages.append(
                sender + ":\n " + message + "\n");
    }

    public void processError(String errorMessage)
    {
        this.receivedMessages.append("ERROR: " +
                errorMessage + "\n");
    }
    
    public void processInfo(String infoMessage)
    {
        this.receivedMessages.append(
                infoMessage + "\n");
    }
}
