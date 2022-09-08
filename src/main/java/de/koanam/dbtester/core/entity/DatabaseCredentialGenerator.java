package de.koanam.dbtester.core.entity;

public interface DatabaseCredentialGenerator {

    String generateUsername();

    String generatePassword();

}
