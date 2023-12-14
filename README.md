# FinkUp
FinkUp is an Android mobile application that utilizes SQLite with Room to locally store user thoughts and Retrofit with the https://github.com/mohdiop/finkup-rest-api project to simulate server storage.

## What is a fink?
A `Fink` is a thought that a user can express as a note entry. It consists of a title, content, and a date indicating the last date it was created or modified.

The code below indicates the data class in which is store the Fink objects:
```kotlin
@Entity
data class Fink(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("finkId") val finkId: Long = 0,
    @ColumnInfo("finkTitle") val finkTitle: String,
    @ColumnInfo("finkContent") val finkContent: String,
    @ColumnInfo("finkDate") val finkDate: Long
)
```
And its graphic representation:

![fink](https://github.com/mohdiop/finkup/assets/122661826/19d831d0-c1b7-4883-b6ca-02cc283422ec)

The date is stored in `Long` as milliseconds and provides by the 
```kotlin 
System.currentTimeMillis()
```
function.

## Functionalities

### Home
It is the main view and provides the list of all the finks you added:

<p align="center">
  <img src="https://github.com/mohdiop/finkup/assets/122661826/cd9e7c8e-e1e6-4eb4-9a5c-aa114a67d3aa" alt="Home" width="auto" height="400"/>
</p>

### Add
In this interface you can add a new fink by giving its title and its content. The current system time will be the `finkDate`:

<p align="center"> 
  <img src="https://github.com/mohdiop/finkup/assets/122661826/be7eb309-9703-493a-bd45-840444255380" alt="Add1" width="auto" height="400"/>
  <img src="https://github.com/mohdiop/finkup/assets/122661826/e6e1b187-b8ec-4e84-ad92-b9fbe335a94c" alt="Add2" width="auto" height="400"/>
  <img src="https://github.com/mohdiop/finkup/assets/122661826/842549df-e2d6-422b-b54c-5f7433188ca1" alt="Add3" width="auto" height="400"/>
</p>

### Update
`On click` in a fink at the home interface you will be able to view it and then modify it:

<p align="center"> 
  <img src="https://github.com/mohdiop/finkup/assets/122661826/99879e8b-4c63-4fea-ad44-47b593a9f392" alt="Update1" width="auto" height="400"/>
  <img src="https://github.com/mohdiop/finkup/assets/122661826/b9ea9b56-fe36-481a-a10d-56ff7feea332" alt="Update2" width="auto" height="400"/>
</p>

### Delete
#### Delete one

`On long click` in the fink item you will be able to view it as the `On click` functionality or delete it:

<p align="center"> 
  <img src="https://github.com/mohdiop/finkup/assets/122661826/70a0e78c-7308-423d-a4e9-40b7623e22d2" alt="Delete" width="auto" height="400"/>
</p>

#### Delete all

By clicking on the trash button you will be able to delete all the finks:

<p align="center"> 
  <img src="https://github.com/mohdiop/finkup/assets/122661826/1dce1d2d-42f7-4ec2-928e-b5a15caa086c" alt="DeleteAll1" width="auto" height="400"/>
  <img src="https://github.com/mohdiop/finkup/assets/122661826/0a74f012-47a9-4045-aad7-783a1fbe3b9b" alt="DeleteAll2" width="auto" height="400"/>
</p>

### Search

In the home section you can search a fink by its title or its content, search response will be order by the current modified fink:

<p align="center"> 
  <img src="https://github.com/mohdiop/finkup/assets/122661826/128b1f60-4ef6-4716-b119-d7ffcdf6d434" alt="Search1" width="auto" height="400"/>
  <img src="https://github.com/mohdiop/finkup/assets/122661826/3ee268d9-7402-4bfb-8d68-9265b3fd9c2b" alt="Search2" width="auto" height="400"/>
  <img src="https://github.com/mohdiop/finkup/assets/122661826/6ccc08e6-1201-4a20-ae49-0f1edff4710f" alt="Search3" width="auto" height="400"/>
</p>

To store data in the Spring Boot server make sure to config the https://github.com/mohdiop/finkup-rest-api project.
