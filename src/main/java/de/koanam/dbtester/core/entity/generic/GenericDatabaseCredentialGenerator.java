package de.koanam.dbtester.core.entity.generic;

import de.koanam.dbtester.core.entity.DatabaseCredentialGenerator;

import java.util.Random;

public class GenericDatabaseCredentialGenerator implements DatabaseCredentialGenerator {

    private static final int ASCII_MIN_LIMIT = 48;
    private static final int ASCII_MAX_LIMIT = 122;

    private Random random = new Random();

    @Override
    public String generateUsername() {
        return this.generateRandomString(10);
    }

    @Override
    public String generatePassword() {
        return this.generateRandomString(15);
    }

    private String generateRandomString(int size){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<size; i++){
            char randomChar = (char) random.nextInt(ASCII_MIN_LIMIT,ASCII_MAX_LIMIT);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

}
