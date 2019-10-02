package com.backendutils;

import java.util.Scanner;

public class EnvSetup {

    public static Integer getEnvironmentId(){
        System.out.println("Please, specify environment number, where you are going to perform testing: \n 1 - QA \n 2 - STG \n 3 - DEV \n 4 - DEV_INT \n 5 - PROD \n");
        Scanner sc = new Scanner(System.in);
        Integer environmentId = sc.nextInt();
        return environmentId;
    }

    public static Env.Postgres setupPostgresEnvironment(Integer envId){
        switch(envId){
            case 1: System.out.println("QA Environment selected"); return Env.Postgres.QA;
            case 2: System.out.println("STG Environment selected"); return Env.Postgres.STG;
            case 3: System.out.println("DEV Environment selected"); return Env.Postgres.DEV;
            case 4: System.out.println("DEV_INT Environment selected"); return Env.Postgres.DEV_INT;
            case 5: System.out.println("PROD Environment selected"); return Env.Postgres.PROD;
            default: System.out.println("QA Environment selected as default"); return Env.Postgres.QA;
        }
    }
}
