package com.spring.pattern.observer.gperadvice;

import java.util.Observable;

/**
 * Created by shanwei on 2019/4/14.
 */
public class Gper extends Observable {

    private String name = "GPer生态圈";
    private static Gper gper = null;
    private Gper(){}

    public static Gper getInstance(){

        if (gper==null){
            gper = new Gper();
        }

        return gper;
    }

    public String getName() {
        return name;
    }

    public void publishQuestion(Question question){
        System.out.println(question.getUserName() + "在" + this.name + "上提交了一个问题。");
        setChanged();
        notifyObservers(question);
    }
}
