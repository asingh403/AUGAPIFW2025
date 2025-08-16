# API Test Automation Framework (WIP)

⚠️ **Status**: This framework is still under development. Features and documentation may change.

---

## Overview
This framework is designed to test RESTful APIs with a modular layered architecture.  
Current layers: **Test Layer**, **Client Layer**, **Configuration & Constants**, **Data Layer**, and **Utilities** (planned).

---

## Architecture

### 1. Test Layer
- **BaseTest** (setup)  
- **GetUserTest**, **CreateUserTest**, **UpdateUserTest**  
- More tests *planned*: DeleteUserTest, Serialization tests, Contacts API, etc.

### 2. Client Layer
- **RestClient** for HTTP methods (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`)  
- Handles auth, request/response specs, query/path params  

### 3. Configuration
- **ConfigManager** loads properties dynamically  
- **config.properties** stores tokens, credentials, base URLs  
- *Planned*: `AuthType Enum`, `ContentType Enum`

### 4. Data Layer
- **User POJO**  
- *Planned*: Product POJO, Contact POJO, Serialization/Deserialization with Jackson

### 5. Utilities (Planned)
- Logger utility  
- Assertions utility  

---

## Flow Summary
1. `ConfigManager` loads configuration (keys, tokens, base URLs).  
2. `BaseTest` initializes the `RestClient`.  
3. `RestClient` executes HTTP requests.  
4. Responses map into POJOs.  
5. Tests run assertions (utilities planned).  

---

## Tech Stack
- Java 17  
- RestAssured  
- Jackson  
- TestNG / JUnit  
- Maven / Gradle  

---

## Visual Diagram (WIP + Planned)
```mermaid
graph TB
    %% Test Layer
    subgraph "Test Layer"
        BT[BaseTest ✅]
        GUT[GetUserTest ✅]
        CUT[CreateUserTest ✅]
        UUT[UpdateUserTest ✅]
        DUT[DeleteUserTest *planned*]
        GST[GetAUserWithSerializationTest *planned*]
        CAT[ContactsAPITest *planned*]
        BAT[BasicAuthTest *planned*]
        PAT[ProductAPITest *planned*]
        RRT[ReqResTest *planned*]
    end

    %% Client Layer
    subgraph "Client Layer"
        RC[RestClient ✅]
    end

    %% Configuration
    subgraph "Configuration"
        CM[ConfigManager ✅]
        CP[config.properties ✅]
        
        subgraph "Constants"
            AT[AuthType Enum *planned*]
            CT[ContentType Enum *planned*]
        end
    end

    %% Data Layer
    subgraph "Data Layer"
        U[User POJO ✅]
        P[Product POJO *planned*]
        C[Contact POJO *planned*]
        SD[Serialization/Deserialization *planned*]
    end

    %% Utilities
    subgraph "Utilities"
        L[Logger Utility *planned*]
        A[Assertions Utility *planned*]
    end

    %% Relations
    BT --> RC
    RC --> CM
    CM --> CP
    CM --> AT
    CM --> CT
    RC --> U
    RC --> P
    RC --> C
    RC --> SD
    BT --> L
    BT --> A
```
   
## Roadmap
- [ ] Add DeleteUserTest  
- [ ] Add serialization/deserialization tests  
- [ ] Implement Product & Contact POJOs  
- [ ] Build Logger and Assertion utilities

_**Current status:** Modules marked "✅" are implemented. Others are pending implementation._
 
