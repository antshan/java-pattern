package com.spring.pattern.observer.guava;

import com.google.common.eventbus.EventBus;

/**
 * Created by shanwei on 2019/4/14.
 */
public class GuavaObserverTest {

    public static void main(String[] args) {

        EventBus eventBus = new EventBus();

        Teacher tom = new Teacher("Tom");
        Teacher mic = new Teacher("Mic");

        eventBus.register(tom);
        eventBus.register(mic);

        Gper gper = Gper.getInstance();
        Question question = new Question();
        question.setUserName("小明");
        question.setContent("观察者设计模式适用于哪些场景？");
        gper.publishQuestion(question);

        eventBus.post(question);
    }
}
