/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Program;
import java.util.Scanner;
class Comment {
    String text;
    int userId;
    Comment next;

    public Comment(String text, int userId) {
        this.text = text;
        this.userId = userId;
        this.next = null;
    }
}
class Post {
    String caption;
    int likes;
    int[] likedBy;
    Comment commentHead;
    Post next;

    public Post(String caption) {
        this.caption = caption;
        this.likes = 0;
        this.likedBy = new int[100]; // Array to track user IDs who liked the post.
        this.commentHead = null;
        this.next = null;
    }

    public boolean like(int userId) {
        for (int id : likedBy) {
            if (id == userId) {
                return false; // User already liked the post.
            }
        }
        for (int i = 0; i < likedBy.length; i++) {
            if (likedBy[i] == 0) {
                likedBy[i] = userId;
                likes++;
                return true;
            }
        }
        return false; // No space left in the likedBy array.
    }

    public boolean removeLike(int userId) {
        for (int i = 0; i < likedBy.length; i++) {
            if (likedBy[i] == userId) {
                likedBy[i] = 0;
                likes--;
                return true;
            }
        }
        return false; // User has not liked the post.
    }

    public void addComment(String text, int userId) {
        Comment newComment = new Comment(text, userId);
        if (commentHead == null) {
            commentHead = newComment;
        } else {
            Comment temp = commentHead;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newComment;
        }
    }

    public boolean deleteComment(int userId) {
        if (commentHead == null) return false;

        if (commentHead.userId == userId) {
            commentHead = commentHead.next;
            return true;
        }

        Comment temp = commentHead;
        while (temp.next != null) {
            if (temp.next.userId == userId) {
                temp.next = temp.next.next;
                return true;
            }
            temp = temp.next;
        }

        return false; // Comment not found
    }
}
class User {
    int userId;
    String password;
    Post postHead;

    public User(int userId, String password) {
        this.userId = userId;
        this.password = password;
        this.postHead = null;
    }

    public void addPost(String caption) {
        Post newPost = new Post(caption);
        if (postHead == null) {
            postHead = newPost;
        } else {
            Post temp = postHead;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newPost;
        }
    }

    public boolean deletePost(int postIndex) {
        if (postIndex == 1 && postHead != null) {
            postHead = postHead.next;
            return true;
        }

        Post temp = postHead;
        int currentIndex = 1;

        while (temp != null && temp.next != null) {
            if (currentIndex == postIndex - 1) {
                temp.next = temp.next.next;
                return true;
            }
            temp = temp.next;
            currentIndex++;
        }

        return false;
    }
}
class UserNode {
    User user;
    UserNode next;

    public UserNode(User user) {
        this.user = user;
        this.next = null;
    }
}
class UserHashTable
{
    private UserNode[] table;
    private int size;
    private int elementCount;

    public UserHashTable(int initialSize) {
        this.size = initialSize;
        this.table = new UserNode[size];
        this.elementCount = 0;
    }

    private int hash(int userId) {
        return Math.abs(Integer.valueOf(userId).hashCode()) % size;
    }


    public boolean addUser(int userId, String password) {
        if (getUser(userId) != null) {
            return false;
        }

        if ((double) elementCount / size > 0.7) {
            resizeTable();
        }

        int index = hash(userId);
        User newUser = new User(userId, password);
        UserNode newNode = new UserNode(newUser);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            UserNode current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        elementCount++;
        return true;
    }

    public User getUser(int userId) {
        int index = hash(userId);
        UserNode current = table[index];
        while (current != null) {
            if (current.user.userId == userId) {
                return current.user;
            }
            current = current.next;
        }
        return null;
    }

    public void displayAllFeeds() {
        System.out.println("\n===== All Feeds =====\n");
        for (UserNode node : table) {
            while (node != null) {
                User user = node.user;
                System.out.println("User ID: " + user.userId);
                System.out.println("------------------------------");
                Post temp = user.postHead;
                int postNumber = 1;
                while (temp != null) {
                    System.out.println("  " + postNumber + ". " + temp.caption + " (Likes: " + temp.likes + ")");
                    if (temp.commentHead != null) {
                        System.out.println("    Comments:");
                        Comment commentTemp = temp.commentHead;
                        while (commentTemp != null) {
                            System.out.println("      - [User ID: " + commentTemp.userId + "] " + commentTemp.text);
                            commentTemp = commentTemp.next;
                        }
                    }
                    temp = temp.next;
                    postNumber++;
                }
                node = node.next;
            }
        }
    }

    private void resizeTable() {
        int newSize = size * 2;
        UserNode[] newTable = new UserNode[newSize];

        for (int i = 0; i < size; i++) {
            UserNode current = table[i];
            while (current != null) {
                int newIndex = hash(current.user.userId);
                UserNode newNode = new UserNode(current.user);
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = newNode;
                } else {
                    UserNode temp = newTable[newIndex];
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                    temp.next = newNode;
                }
                current = current.next;
            }
        }

        table = newTable;
        size = newSize;
    }
}
public class Program
{
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserHashTable userHashTable = new UserHashTable(5);
        User loggedInUser = null;

        while (true) {
            if (loggedInUser == null) {
                loggedInUser = loginMenu(userHashTable);
            } else {
                loggedInUser = userMenu(loggedInUser, userHashTable);
            }
        }
    }

    private static User loginMenu(UserHashTable userHashTable) {
        System.out.println("\nSocial Media Feed - Login");
        System.out.println("1. Login");
        System.out.println("2. Create New Account");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                return loginUser(userHashTable);

            case 2:
                createAccount(userHashTable);
                return null;

            case 3:
                System.out.println("Exiting... Goodbye!");
                System.exit(0);
                return null;

            default:
                System.out.println("Invalid choice. Please try again.");
                return null;
        }
    }

    private static User userMenu(User loggedInUser, UserHashTable userHashTable) {
        System.out.println("\nSocial Media Feed - User Menu");
        System.out.println("1. Add Post");
        System.out.println("2. Like or Comment");
        System.out.println("3. View Feed");
        System.out.println("4. View All Feeds");
        System.out.println("5. Delete Post");
        System.out.println("6. Logout");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                addPost(loggedInUser);
                break;

            case 2:
                likeOrComment(userHashTable, loggedInUser);
                break;

            case 3:
                viewFeed(loggedInUser);
                break;

            case 4:
                userHashTable.displayAllFeeds();
                break;

            case 5:
                deletePost(loggedInUser);
                break;

            case 6:
                System.out.println("Logged out successfully.");
                return null;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return loggedInUser;
    }

    private static User loginUser(UserHashTable userHashTable) {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        User user = userHashTable.getUser(userId);

        if (user == null) {
            System.out.println("User not found.");
        } else {
            System.out.print("Enter Password: ");
            scanner.nextLine();
            String password = scanner.nextLine();
            if (!user.password.equals(password)) {
                System.out.println("Incorrect password.");
            } else {
                System.out.println("Login successful. Welcome, User ID: " + user.userId);
                return user;
            }
        }
        return null;
    }

    private static void createAccount(UserHashTable userHashTable) {
        System.out.print("Enter New User ID: ");
        int newUserId = scanner.nextInt();
        System.out.print("Enter Password: ");
        scanner.nextLine();
        String newPassword = scanner.nextLine();

        if (userHashTable.addUser(newUserId, newPassword)) {
            System.out.println("Account created successfully! Your User ID is " + newUserId);
        } else {
            System.out.println("User ID already exists. Please try another ID.");
        }
    }

    private static void addPost(User loggedInUser) {
        System.out.print("Enter Post Caption: ");
        scanner.nextLine();
        String caption = scanner.nextLine();
        loggedInUser.addPost(caption);
        System.out.println("Post added.");
    }

    private static void likeOrComment(UserHashTable userHashTable, User loggedInUser) {
        System.out.println("1. Like");
        System.out.println("2. Comment");
        System.out.println("3. Delete Comment");
        System.out.println("4. Delete like");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                likePost(userHashTable, loggedInUser);
                break;

            case 2:
                commentOnPost(userHashTable, loggedInUser);
                break;

            case 3:
                deleteComment(userHashTable, loggedInUser);
                break;

            case 4:
                deletelike(userHashTable, loggedInUser);
                break;
            case 5:
                return;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void likePost(UserHashTable userHashTable, User loggedInUser) {
        System.out.println("Select a post to like:");
        userHashTable.displayAllFeeds();

        System.out.print("Enter the User ID of the post owner: ");
        int postOwnerId = scanner.nextInt();
        User postOwner = userHashTable.getUser(postOwnerId);

        if (postOwner == null) {
            System.out.println("User not found.");
        } else {
            Post temp = postOwner.postHead;
            int postNumber = 1;

            while (temp != null) {
                System.out.println(postNumber + ". " + temp.caption + " (Likes: " + temp.likes + ")");
                temp = temp.next;
                postNumber++;
            }

            System.out.print("Select the post number to like: ");
            int postIndex = scanner.nextInt();
            temp = postOwner.postHead;

            for (int i = 1; i < postIndex && temp != null; i++) {
                temp = temp.next;
            }

            if (temp != null) {
                if (temp.like(loggedInUser.userId)) {
                    System.out.println("Post liked successfully!");
                } else {
                    System.out.println("You have already liked this post.");
                }
            } else {
                System.out.println("Invalid post number.");
            }
        }
    }

    private static void commentOnPost(UserHashTable userHashTable, User loggedInUser) {
        System.out.println("Select a post to comment on:");
        userHashTable.displayAllFeeds();

        System.out.print("Enter the User ID of the post owner: ");
        int postOwnerId = scanner.nextInt();
        User postOwner = userHashTable.getUser(postOwnerId);

        if (postOwner == null) {
            System.out.println("User not found.");
        } else {
            Post temp = postOwner.postHead;
            int postNumber = 1;

            while (temp != null) {
                System.out.println(postNumber + ". " + temp.caption + " (Likes: " + temp.likes + ")");
                temp = temp.next;
                postNumber++;
            }

            System.out.print("Select the post number to comment on: ");
            int postIndex = scanner.nextInt();
            temp = postOwner.postHead;

            for (int i = 1; i < postIndex && temp != null; i++) {
                temp = temp.next;
            }

            if (temp != null) {
                System.out.print("Enter your comment: ");
                scanner.nextLine();
                String commentText = scanner.nextLine();
                temp.addComment(commentText, loggedInUser.userId);
                System.out.println("Comment added successfully!");
            } else {
                System.out.println("Invalid post number.");
            }
        }
    }

    private static void deleteComment(UserHashTable userHashTable, User loggedInUser) {
        System.out.println("Select a post to delete a comment on:");
        userHashTable.displayAllFeeds();

        System.out.print("Enter the User ID of the post owner: ");
        int postOwnerId = scanner.nextInt();
        User postOwner = userHashTable.getUser(postOwnerId);

        if (postOwner == null) {
            System.out.println("User not found.");
        } else {
            Post temp = postOwner.postHead;
            int postNumber = 1;

            while (temp != null) {
                System.out.println(postNumber + ". " + temp.caption + " (Likes: " + temp.likes + ")");
                temp = temp.next;
                postNumber++;
            }

            System.out.print("Select the post number to delete comment: ");
            int postIndex = scanner.nextInt();
            temp = postOwner.postHead;

            for (int i = 1; i < postIndex && temp != null; i++) {
                temp = temp.next;
            }

            if (temp != null) {
                boolean success = temp.deleteComment(loggedInUser.userId);
                if (success) {
                    System.out.println("Comment deleted successfully!");
                } else {
                    System.out.println("No comment found to delete.");
                }
            } else {
                System.out.println("Invalid post number.");
            }
        }
    }

    private static void deletelike(UserHashTable userHashTable, User loggedInUser) {
        System.out.println("Select a post to remove your like:");
        userHashTable.displayAllFeeds();

        System.out.print("Enter the User ID of the post owner: ");
        int postOwnerId = scanner.nextInt();
        User postOwner = userHashTable.getUser(postOwnerId);

        if (postOwner == null) {
            System.out.println("User not found.");
        } else {
            Post temp = postOwner.postHead;
            int postNumber = 1;

            while (temp != null) {
                System.out.println(postNumber + ". " + temp.caption + " (Likes: " + temp.likes + ")");
                temp = temp.next;
                postNumber++;
            }

            System.out.print("Select the post number to remove your like: ");
            int postIndex = scanner.nextInt();
            temp = postOwner.postHead;

            for (int i = 1; i < postIndex && temp != null; i++) {
                temp = temp.next;
            }

            if (temp != null) {
                boolean success = temp.removeLike(loggedInUser.userId);
                if (success) {
                    System.out.println("Like removed successfully!");
                } else {
                    System.out.println("You have not liked this post.");
                }
            } else {
                System.out.println("Invalid post number.");
            }
        }
    }



    private static void viewFeed(User loggedInUser) {
        System.out.println("Your Feed:");
        Post temp = loggedInUser.postHead;
        int postNumber = 1;

        while (temp != null) {
            System.out.println(postNumber + ". " + temp.caption);
            System.out.println("Likes: " + temp.likes);
            System.out.println("Comments: " + (temp.commentHead == null ? "None" : ""));
            Comment commentTemp = temp.commentHead;
            while (commentTemp != null) {
                System.out.println("[User ID: " + commentTemp.userId + "] " + commentTemp.text);
                commentTemp = commentTemp.next;
            }

            temp = temp.next;
            postNumber++;
        }

        if (postNumber == 1) {
            System.out.println("No posts available.");
        }
    }

    private static void deletePost(User loggedInUser) {
        System.out.println("Select a post to delete:");
        Post temp = loggedInUser.postHead;
        int postNumber = 1;

        while (temp != null) {
            System.out.println(postNumber + ". " + temp.caption);
            temp = temp.next;
            postNumber++;
        }

        if (postNumber == 1) {
            System.out.println("No posts to delete.");
        } else {
            System.out.print("Enter the post number to delete: ");
            int deletePostIndex = scanner.nextInt();

            if (loggedInUser.deletePost(deletePostIndex)) {
                System.out.println("Post deleted successfully.");
            } else {
                System.out.println("Invalid post number.");
            }
        }
    }
}