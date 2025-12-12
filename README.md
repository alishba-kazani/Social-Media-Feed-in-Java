# Social-Media-Feed-in-Java
Social Media Feed (Java Project)
Introduction
This project is a simple text-based social media feed implemented in Java using hash tables and linked lists. Users can create accounts, add posts, like and comment on posts, and view all feeds.

Features
User Management: Users can create accounts, log in, and manage their posts.
Post System: Users can add, delete, and view their posts.
Like System: Users can like or remove their like from posts.
Comment System: Users can comment on posts and delete their own comments.
Feed Display: Users can view their own posts and all users' posts.
Data Structures Used
Hash Table: Used to store users efficiently.
Linked List: Used to manage posts and comments.
Dynamic Resizing: The hash table resizes when 70% full to maintain efficiency.
Classes Overview
Comment
Represents a comment on a post.

text: The comment text.
userId: The user who posted the comment.
next: Pointer to the next comment.
Post
Represents a social media post.

caption: The post content.
likes: Count of likes.
likedBy: Array tracking user IDs who liked the post.
commentHead: Pointer to the first comment.
next: Pointer to the next post.
User
Represents a user account.

userId: Unique ID.
password: User password.
postHead: Head of the linked list of posts.
UserNode
Node structure for hash table chaining.

user: The user data.
next: Pointer to the next node in case of collision.
UserHashTable
Stores and manages users.

table: Array of user nodes.
addUser(int userId, String password): Adds a new user.
getUser(int userId): Retrieves a user by ID.
displayAllFeeds(): Displays all posts and comments.
Resizing Logic: Doubles size if more than 70% full.
User Menu Options
1. Add Post - Create a new post.
2. Like or Comment - Like, comment, delete comment, or remove like.
3. View Feed - View your posts.
4. View All Feeds - View all users' posts and interactions.
5. Delete Post - Remove one of your posts.
6. Logout - Log out of the system.
Future Enhancements
Implement a Graphical User Interface (GUI).
Allow password encryption for better security.
Improve data persistence with file storage or a database.
Optimize memory usage in likedBy array.
Conclusion
This project is a great introduction to implementing hash tables, linked lists, and user interactions in Java. It provides a basic but functional social media feed system that can be expanded and optimized further.
