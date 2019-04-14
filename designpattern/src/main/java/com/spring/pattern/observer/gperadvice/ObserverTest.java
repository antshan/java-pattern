package com.spring.pattern.observer.gperadvice;

/**
 * Created by shanwei on 2019/4/14.
 */
public class ObserverTest {

    public static void main(String[] args) {

        Gper gper = Gper.getInstance();
        Teacher tom = new Teacher("Tom");
        Teacher mic = new Teacher("Mic");

        Question question = new Question();
        question.setUserName("小明");
        question.setContent("观察者设计模式适用于哪些场景？");

        gper.addObserver(tom);
        gper.addObserver(mic);
        gper.publishQuestion(question);
    }
}
