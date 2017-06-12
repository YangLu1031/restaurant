package com.laioffer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by yanglu on 6/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MockTest {

    @Mock
    private List mockList;

    @Test
    public void verify_behaviour(){
        mockList.add(1);
        verify(mockList).add(1);
    }

    @Test
    public void when_thenReturn(){
//        when(iterator.next()).thenReturn("hello");
//        String result = iterator.next();
        System.out.println(mockList.get(0));
    }
    @Test
    public void with_arguments(){
        Comparable com = mock(Comparable.class);
        when(com.compareTo("Test")).thenReturn(1);

        assertEquals(0, com.compareTo("Not stub"));
        assertEquals(1, com.compareTo("Test"));
    }
    @Test
    public void with_unspecific_arguments(){
        when(mockList.get(anyInt())).thenReturn(1);
        when(mockList.contains(argThat(new IsValid()))).thenReturn(true);

        assertEquals(1, mockList.get(1));
        assertEquals(1, mockList.get(999));
        assertTrue(mockList.contains(1));
        assertTrue(mockList.contains(2));
    }

    private class IsValid extends ArgumentMatcher<List>{
        @Override
        public boolean matches(Object o){
            return o.equals(1) || o.equals(2) ;
        }
    }

    @Test
    public void all_arguments_provided_by_matchers(){
        Comparator comparator = mock(Comparator.class);
        comparator.compare("nihao","hello");
        //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
//        verify(comparator).compare(anyString(),eq("hello"));
        //下面的为无效的参数匹配使用
//        verify(comparator).compare(anyString(),"hello");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void spy_on_real_objects(){
        List list = new LinkedList();
        List spy = spy(list);
        //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
        when(spy.get(0)).thenReturn(3);
        assertEquals(1,spy.get(0));

        //使用doReturn-when可以避免when-thenReturn调用真实对象api
        doReturn(999).when(spy).get(999);
        //预设size()期望值
        when(spy.size()).thenReturn(100);
        //调用真实对象的api
        spy.add(1);
        spy.add(2);
        assertEquals(100,spy.size());
        assertEquals(1,spy.get(0));
        assertEquals(2,spy.get(1));
        verify(spy).add(1);
        verify(spy).add(2);
        assertEquals(999,spy.get(999));
        spy.get(2);
    }

}
