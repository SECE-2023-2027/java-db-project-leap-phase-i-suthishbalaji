package com.arg.bank;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Sample {
    public static void main(String[] args) {
        List<Integer> num=new ArrayList();
        num.add(1);
        num.add(2);
        num.add(2);
        num.add(3);
        num.add(3);
        num.add(3);
        num.add(4);
        num.add(4);
        num.add(5);
        System.out.println(num);
        List<Object> duplicates = new ArrayList<Object>();
        for (int x = 0; x < num.size(); x++) {
          for (int y = x + 1; y < num.size(); y++) 
          {
            if (num.get(x).equals(num.get(y))) 
            {
             duplicates.add(num.get(x));
             break;
            }
          }
        }
        System.out.println(duplicates);
    }
}