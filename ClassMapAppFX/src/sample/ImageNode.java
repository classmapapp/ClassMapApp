package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.lang.System;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.io.File;

/**
 * Created by James Davis on 12/19/2015.
 */
public class ImageNode extends MapNode {
    private GridPane nodePane;
    private byte[] imgToByte;
    private String formattedDate;
    private Image image;

    public ImageNode(String in)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        this.type = type.image;
        this.setUserVote(Boolean.TRUE);
        this.incrementVoteCounter();
        this.createdBy = DataConnection.loggedUser.getUser();
        this.nodePerm = DataConnection.loggedUser.getAccount();
        image = new Image(in);

        this.drawNode();



    }

    public ImageNode(File in)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        formattedDate = sdf.format(date);
        this.type = type.image;
        this.setUserVote(Boolean.TRUE);
        this.incrementVoteCounter();
        this.createdBy = DataConnection.loggedUser.getUser();
        this.nodePerm = DataConnection.loggedUser.getAccount();
        this.image = new Image(in.toURI().toString());
        this.drawNode();

    }

    public ImageNode(int id, int pid, Timestamp created,String user, String accountType)
    {
        this.type = type.image;
        uniqueId = id;
        parent = pid;
        timeCreated = created;
        this.createdBy = user;
        this.nodePerm = accountType;
    }

    public void drawNode() {
        ImageView viewer = new ImageView(image);
        viewer.setPreserveRatio(Boolean.TRUE);
        viewer.setFitHeight(80.0f);
        double height = viewer.getBoundsInParent().getHeight();
        double width = viewer.getBoundsInParent().getWidth() * 2/3;

        Ellipse newNode = new Ellipse(0.0f, 0.0f, width, height);
        if(this.nodePerm.equals("student")) {
            newNode.setFill(Paint.valueOf("white"));
            newNode.setStroke(Paint.valueOf("black"));
        }
        else {
            newNode.setFill(Paint.valueOf("black"));
            newNode.setStroke(Paint.valueOf("black"));
        }

        Image arrow;
        ImageView arrowView;
        if(this.getUserVote() == Boolean.TRUE)
        {
            File path = new File("./Images/arrow-up-icon_voted.png");
            arrow = new Image(path.toURI().toString());

        }
        else
        {
            File path = new File("./Images/arrow-up-icon.png");
            arrow = new Image(path.toURI().toString());
        }

        arrowView = new ImageView(arrow);
        arrowView.setPreserveRatio(Boolean.TRUE);
        arrowView.setFitHeight(20.0f);
        Text numberOfVotes = new Text(" "+votes);
        numberOfVotes.setStyle("-fx-font: 20 arial");
        HBox arr = new HBox();
        arr.getChildren().addAll(arrowView,numberOfVotes);

        arr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(getUserVote() == Boolean.TRUE)
                {
                    File path = new File("./Images/arrow-up-icon.png");
                    Image newArrow = new Image(path.toURI().toString());
                    ImageView newArrowView = new ImageView(newArrow);
                    newArrowView.setPreserveRatio(Boolean.TRUE);
                    newArrowView.setFitHeight(20.0f);
                    decrementVoteCounter();
                    Text numberOfVotes = new Text(""+(votes));
                    numberOfVotes.setStyle("-fx-font: 20 arial");

                    arr.getChildren().remove(0, 2);
                    arr.getChildren().addAll(newArrowView, numberOfVotes);
                    setUserVote(false);
                    try {
                        sendSelf();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //setVisible();

                }
                else
                {
                    File path = new File("./Images/arrow-up-icon_voted.png");
                    Image newArrow = new Image(path.toURI().toString());
                    ImageView newArrowView = new ImageView(newArrow);
                    newArrowView.setPreserveRatio(Boolean.TRUE);
                    newArrowView.setFitHeight(20.0f);
                    incrementVoteCounter();
                    Text numberOfVotes = new Text(""+(votes));
                    numberOfVotes.setStyle("-fx-font: 20 arial");
                    arr.getChildren().remove(0, 2);
                    arr.getChildren().addAll(newArrowView, numberOfVotes);
                    setUserVote(true);
                    try {
                        sendSelf();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //setVisible();
                }
            }
        });

        StackPane stack = new StackPane();
        stack.getChildren().addAll(newNode, viewer);

        nodePane = new GridPane();
        nodePane.add(arr,0,0);
        nodePane.add(stack,0,1);

        nodePane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                nodePane.setTranslateX(event.getSceneX() - nodePane.getHeight()/2);
                nodePane.setTranslateY(event.getSceneY() - nodePane.getWidth()/2);
            }
        });
    }

    public void setTypeToImage()
    {
        this.type = classification.image;
    }

    public GridPane getNodePane()
    {
        return this.nodePane;
    }

    public String getFormattedDate() { return this.formattedDate; }

    public byte[] imageToByteArray()
    {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "jpg", s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.toByteArray();

    }

    public void sendSelf() throws SQLException { DataConnection.addUpvote(this); }

    public void setImage(Image node) { this.image = node; }

    public String getAccountPerm() { return this.nodePerm; }

    public void makeVisible() {
        this.nodePane.setVisible(true);
    }

    public void setVisible() {
        if (getUserVote() == true) {
            this.nodePane.setVisible(true);
        }
        else
            this.nodePane.setVisible(false);
    }

}
