package model;

/**
 * Created by leandredeguerville on 02/07/14.
 */
public class NavDrawerItem {
    private String title;
    private int icon;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(){}

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title, int icon, String count){
        this.title = title;
        this.icon = icon;
        if (Integer.parseInt(count) > 0){
            this.isCounterVisible = true;
        }
        else {
            this.isCounterVisible = false;
        }
        this.count = count;
        if (Integer.parseInt(count) > 50){
            this.count = "50+";
        }
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }

}
