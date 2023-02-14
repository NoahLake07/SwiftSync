import com.swiftsync.Pages;
import com.swiftsync.panels.*;
import freshui.FreshUI;
import freshui.graphics.FPanel;

import java.awt.*;

public class Main extends FreshUI implements Pages {

    int page = DASHBOARD;

    Dashboard dashboard = new Dashboard();
    public void init(){
        this.setProgramName("SwiftSync");
        this.setSize(650,525);
        this.setBackground(new Color(248, 248, 248));

        add(dashboard,0,0);
    }

    public void updatePages(){
        switch(page){

        }
    }

    public void setPage(int p){
        page = p;
        updatePages();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}