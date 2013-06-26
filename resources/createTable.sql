--Drops:
drop table if exists bpmn.RolePlay CASCADE;
drop table if exists bpmn.DecisionGlossary CASCADE;
drop table if exists bpmn.ActionGlossary CASCADE;
drop table if exists bpmn.ReasonGlossary CASCADE;
drop table if exists bpmn.MailBox CASCADE;
drop table if exists bpmn.Userdb CASCADE;
drop table if exists bpmn.Document CASCADE;
drop table if exists bpmn.ProcessStep CASCADE;
drop table if exists bpmn.Document_ProcessStep CASCADE;

drop sequence if exists bpmn.roleplay_id_seq;
drop sequence if exists bpmn.document_id_seq;
drop sequence if exists bpmn.actionGlossary_id_seq;
drop sequence if exists bpmn.decisionGlossary_id_seq;
drop sequence if exists bpmn.reasonGlossary_id_seq;
drop sequence if exists bpmn.document_ProcessStep_id_seq;
drop sequence if exists bpmn.processStep_id_seq;
drop sequence if exists bpmn.mailBox_id_seq;
drop sequence if exists bpmn.userdb_id_seq;

--Start der Erstellung der DB

--Sequenzen fuer die IDs erstellen
create sequence bpmn.roleplay_id_seq;
create sequence bpmn.document_id_seq;
create sequence bpmn.actionGlossary_id_seq;
create sequence bpmn.decisionGlossary_id_seq;
create sequence bpmn.reasonGlossary_id_seq;
create sequence bpmn.document_ProcessStep_id_seq;
create sequence bpmn.processStep_id_seq;
create sequence bpmn.mailBox_id_seq;
create sequence bpmn.userdb_id_seq;

--Tabellen anlegen
CREATE TABLE
    bpmn.RolePlay
    (
        rolePlayID INTEGER DEFAULT nextval('bpmn.roleplay_id_seq'::regclass) NOT NULL PRIMARY KEY,
        name TEXT NOT NULL,
        currentRoundNo integer NOT NULL,
        status TEXT NOT NULL,
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.ActionGlossary
    (
        actionGlossaryID INTEGER DEFAULT nextval('bpmn.actionGlossary_id_seq'::regclass) NOT NULL PRIMARY KEY,
        name TEXT NOT NULL,
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.DecisionGlossary
    (
        decisionGlossaryID INTEGER DEFAULT nextval('bpmn.decisionGlossary_id_seq'::regclass) NOT NULL PRIMARY KEY,
        name TEXT NOT NULL,
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.ReasonGlossary
    (
        reasonGlossaryID INTEGER DEFAULT nextval('bpmn.reasonGlossary_id_seq'::regclass) NOT NULL PRIMARY KEY,
        name TEXT NOT NULL,
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.MailBox
    (
        mailBoxID INTEGER DEFAULT nextval('bpmn.mailBox_id_seq'::regclass) NOT NULL PRIMARY KEY,
        pushkey TEXT NOT NULL UNIQUE,
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.Userdb
    (
        userID INTEGER DEFAULT nextval('bpmn.userdb_id_seq'::regclass) NOT NULL PRIMARY KEY,
        role TEXT NOT NULL,
        mailboxID INTEGER references bpmn.MailBox(mailBoxID),
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.Document
    (
        documentID INTEGER DEFAULT nextval('bpmn.document_id_seq'::regclass) NOT NULL PRIMARY KEY,
        name TEXT NOT NULL,
        rfidID TEXT NOT NULL,
        mailBoxID INTEGER references bpmn.MailBox(mailBoxID),
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.ProcessStep
    (
        processStepID INTEGER DEFAULT nextval('bpmn.processStep_id_seq'::regclass) NOT NULL PRIMARY KEY,
        name TEXT NOT NULL,
        roundNo INTEGER NOT NULL,
        userID INTEGER references bpmn.userdb(userID),
        actionGlossaryID INTEGER references bpmn.ActionGlossary(actionGlossaryID),
		reasonGlossaryID INTEGER references bpmn.ReasonGlossary(reasonGlossaryID),
		decisionGlossaryID INTEGER references bpmn.DecisionGlossary(decisionGlossaryID),
		rolePlayID INTEGER references bpmn.RolePlay(rolePlayID),
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );

CREATE TABLE
    bpmn.Document_ProcessStep
    (
        document_ProcessStepID INTEGER DEFAULT nextval('bpmn.document_ProcessStep_id_seq'::regclass) NOT NULL PRIMARY KEY,
        documentID INTEGER references bpmn.Document(documentID),
        processStepID INTEGER references bpmn.ProcessStep(processStepID),
        created timestamp NOT NULL,
        updated timestamp NOT NULL
    );
    