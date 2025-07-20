# Java I/O Streams - Complete Study Guide 🚀

## 📚 Table of Contents
1. [I/O Streams Hierarchy](#io-streams-hierarchy)
2. [Real-World Applications](#real-world-applications)
3. [Basic File Operations](#basic-file-operations)
4. [Byte Streams (Binary Data)](#byte-streams-binary-data)
5. [Object Serialization](#object-serialization)
6. [Data Streams (Primitive Types)](#data-streams-primitive-types)
7. [ByteArrayOutputStream](#bytearrayoutputstream)
8. [SequenceInputStream](#sequenceinputstream)
9. [Character Streams (Text Data)](#character-streams-text-data)
10. [FileDescriptor](#filedescriptor)
11. [Advanced Topics](#advanced-topics)
12. [Best Practices & Performance](#best-practices--performance)
13. [Troubleshooting Guide](#troubleshooting-guide)

---

## 📁 I/O Streams Hierarchy

```
java.io Package
├── Byte Streams (Binary Data) - Think: Photos, Videos, Executables
│   ├── Input Streams (Reading)
│   │   ├── FileInputStream          → Read image files, PDFs
│   │   ├── ByteArrayInputStream     → Read from memory buffer
│   │   ├── ObjectInputStream        → Load saved game states
│   │   ├── DataInputStream          → Read binary database records
│   │   └── SequenceInputStream      → Combine multiple log files
│   └── Output Streams (Writing)
│       ├── FileOutputStream         → Save downloaded files
│       ├── ByteArrayOutputStream    → Buffer data for multiple destinations
│       ├── ObjectOutputStream       → Save user preferences
│       └── DataOutputStream         → Write structured binary data
└── Character Streams (Text Data) - Think: Config files, CSV, JSON
    ├── Input Streams (Reading)
    │   ├── FileReader               → Read configuration files
    │   ├── BufferedReader           → Read large text files efficiently
    │   └── InputStreamReader        → Convert byte streams to character streams
    └── Output Streams (Writing)
        ├── FileWriter               → Write log files, reports
        ├── BufferedWriter           → Write large amounts of text efficiently
        └── OutputStreamWriter       → Convert character streams to byte streams
```

---

## 🌍 Real-World Applications

### 🎮 Gaming Applications
```java
// Save game progress
ObjectOutputStream saveGame = new ObjectOutputStream(new FileOutputStream("savegame.dat"));
saveGame.writeObject(playerData);
saveGame.writeObject(gameLevel);
saveGame.writeObject(inventory);
```

### 📊 Data Analysis
```java
// Process CSV files
BufferedReader csvReader = new BufferedReader(new FileReader("sales_data.csv"));
String line;
while ((line = csvReader.readLine()) != null) {
    String[] data = line.split(",");
    processSalesRecord(data);
}
```

### 📱 Mobile App Development
```java
// Cache user preferences
Properties userPrefs = new Properties();
userPrefs.setProperty("theme", "dark");
userPrefs.setProperty("notifications", "enabled");
userPrefs.store(new FileOutputStream("user.properties"), "User Settings");
```

### 🌐 Web Development
```java
// Log HTTP requests
FileWriter logWriter = new FileWriter("access.log", true); // append mode
logWriter.write(timestamp + " - " + ipAddress + " - " + requestPath + "\n");
logWriter.flush();
```

---

## 🔧 **Section 1: Basic File Operations**

### File Class - Your File System Navigator
**Think of it as**: A GPS for your file system - it knows where files are but doesn't read their content.

```java
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperationsDemo {
    public static void main(String[] args) throws IOException {
        // 🏠 Working with directories
        File projectDir = new File("MyProject");
        if (!projectDir.exists()) {
            projectDir.mkdir(); // Create directory
            System.out.println("📁 Created project directory");
        }
        
        // 📄 Creating files
        File configFile = new File(projectDir, "config.txt");
        if (configFile.createNewFile()) {
            System.out.println("✅ Created config file");
        }
        
        // 📊 File information
        System.out.println("📏 File size: " + configFile.length() + " bytes");
        System.out.println("📅 Last modified: " + new Date(configFile.lastModified()));
        System.out.println("🔒 Can read: " + configFile.canRead());
        System.out.println("✏️ Can write: " + configFile.canWrite());
        
        // 📂 List directory contents
        File[] files = projectDir.listFiles();
        if (files != null) {
            System.out.println("📋 Directory contents:");
            for (File file : files) {
                System.out.println("  " + (file.isDirectory() ? "📁" : "📄") + " " + file.getName());
            }
        }
        
        // 🗑️ Cleanup
        configFile.delete();
        projectDir.delete();
    }
}
```

### 🎯 Practical File Operations
```java
public class FileUtilities {
    
    // 📋 Copy file method
    public static void copyFile(String source, String destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[1024]; // 1KB buffer
            int bytesRead;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            System.out.println("✅ File copied successfully!");
        }
    }
    
    // 📏 Get file size in human-readable format
    public static String getFileSize(File file) {
        long bytes = file.length();
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)) + " MB";
        return (bytes / (1024 * 1024 * 1024)) + " GB";
    }
    
    // 🔍 Find files with specific extension
    public static void findFiles(File directory, String extension) {
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        if (files != null) {
            System.out.println("🔍 Found " + files.length + " ." + extension + " files:");
            for (File file : files) {
                System.out.println("  📄 " + file.getName() + " (" + getFileSize(file) + ")");
            }
        }
    }
}
```

---

## 🔧 **Section 2: Byte Streams (Binary Data)**

### 🌟 Real-World Scenario: Image Processing App
```java
import java.io.*;

public class ImageProcessor {
    
    // 📸 Save downloaded image
    public static void saveImage(byte[] imageData, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("images/" + filename)) {
            fos.write(imageData);
            System.out.println("📸 Image saved: " + filename);
        }
    }
    
    // 🖼️ Read image for processing
    public static byte[] loadImage(String filename) throws IOException {
        File imageFile = new File("images/" + filename);
        byte[] imageData = new byte[(int) imageFile.length()];
        
        try (FileInputStream fis = new FileInputStream(imageFile)) {
            fis.read(imageData);
            System.out.println("🖼️ Image loaded: " + filename);
        }
        
        return imageData;
    }
    
    // 🔄 Create thumbnail (simplified example)
    public static void createThumbnail(String originalFile) throws IOException {
        byte[] originalData = loadImage(originalFile);
        
        // Simulate thumbnail creation (in real app, you'd use image processing library)
        byte[] thumbnailData = new byte[originalData.length / 4]; // Simplified
        System.arraycopy(originalData, 0, thumbnailData, 0, thumbnailData.length);
        
        String thumbnailName = "thumb_" + originalFile;
        saveImage(thumbnailData, thumbnailName);
    }
}
```

### 💾 File Download Manager
```java
public class FileDownloader {
    
    public static void downloadFile(String url, String filename) throws IOException {
        // Simulate downloading (in real app, you'd use URL connection)
        byte[] simulatedData = "This is downloaded content".getBytes();
        
        try (FileOutputStream fos = new FileOutputStream("downloads/" + filename);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            
            bos.write(simulatedData);
            System.out.println("⬇️ Downloaded: " + filename);
        }
    }
    
    public static void downloadWithProgress(String url, String filename) throws IOException {
        // Simulate large file download with progress
        byte[] chunk = "data_chunk_".getBytes();
        int totalChunks = 100;
        
        try (FileOutputStream fos = new FileOutputStream("downloads/" + filename)) {
            for (int i = 0; i < totalChunks; i++) {
                fos.write(chunk);
                fos.write(String.valueOf(i).getBytes());
                
                // Show progress
                int progress = (i + 1) * 100 / totalChunks;
                System.out.print("\r📊 Progress: " + progress + "%");
                
                // Simulate network delay
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            }
            System.out.println("\n✅ Download complete!");
        }
    }
}
```

---

## 🔧 **Section 3: Object Serialization**

### 🎮 Game Save System
```java
import java.io.*;
import java.util.*;

// 🎮 Player class with game data
class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String playerName;
    private int level;
    private int health;
    private int experience;
    private List<String> inventory;
    private Map<String, Integer> achievements;
    
    public Player(String name) {
        this.playerName = name;
        this.level = 1;
        this.health = 100;
        this.experience = 0;
        this.inventory = new ArrayList<>();
        this.achievements = new HashMap<>();
    }
    
    // 🎯 Add item to inventory
    public void addItem(String item) {
        inventory.add(item);
        System.out.println("🎒 Added " + item + " to inventory");
    }
    
    // 🏆 Unlock achievement
    public void unlockAchievement(String achievement, int points) {
        achievements.put(achievement, points);
        System.out.println("🏆 Achievement unlocked: " + achievement + " (+" + points + " points)");
    }
    
    // 📊 Display player stats
    public void displayStats() {
        System.out.println("\n🎮 Player Stats:");
        System.out.println("👤 Name: " + playerName);
        System.out.println("🎯 Level: " + level);
        System.out.println("❤️ Health: " + health);
        System.out.println("⭐ Experience: " + experience);
        System.out.println("🎒 Inventory: " + inventory);
        System.out.println("🏆 Achievements: " + achievements);
    }
    
    // Getters and setters
    public void levelUp() { level++; }
    public void gainExperience(int exp) { experience += exp; }
    public void takeDamage(int damage) { health = Math.max(0, health - damage); }
    public void heal(int healAmount) { health = Math.min(100, health + healAmount); }
}

// 💾 Game save/load system
public class GameSaveSystem {
    private static final String SAVE_FILE = "game_save.dat";
    
    // 💾 Save game state
    public static void saveGame(Player player) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(player);
            oos.writeObject(new Date()); // Save timestamp
            System.out.println("💾 Game saved successfully!");
        } catch (IOException e) {
            System.err.println("❌ Failed to save game: " + e.getMessage());
        }
    }
    
    // 📂 Load game state
    public static Player loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Player player = (Player) ois.readObject();
            Date saveTime = (Date) ois.readObject();
            System.out.println("📂 Game loaded from: " + saveTime);
            return player;
        } catch (FileNotFoundException e) {
            System.out.println("ℹ️ No save file found. Starting new game.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Failed to load game: " + e.getMessage());
            return null;
        }
    }
    
    // 🎮 Demo gameplay
    public static void main(String[] args) {
        // Try to load existing game
        Player player = loadGame();
        
        if (player == null) {
            // Create new player
            player = new Player("Hero");
            player.addItem("Sword");
            player.addItem("Shield");
            player.gainExperience(150);
            player.levelUp();
            player.unlockAchievement("First Level", 100);
        }
        
        // Display current stats
        player.displayStats();
        
        // Save game
        saveGame(player);
    }
}
```

### 🏪 Shopping Cart Persistence
```java
import java.io.*;
import java.util.*;

class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, Integer> items;
    private double totalAmount;
    private Date lastModified;
    
    public ShoppingCart() {
        this.items = new HashMap<>();
        this.totalAmount = 0.0;
        this.lastModified = new Date();
    }
    
    public void addItem(String item, int quantity, double price) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
        totalAmount += (quantity * price);
        lastModified = new Date();
        System.out.println("🛒 Added " + quantity + "x " + item + " ($" + price + " each)");
    }
    
    public void displayCart() {
        System.out.println("\n🛒 Shopping Cart:");
        items.forEach((item, quantity) -> 
            System.out.println("  " + quantity + "x " + item));
        System.out.println("💰 Total: $" + String.format("%.2f", totalAmount));
        System.out.println("🕒 Last modified: " + lastModified);
    }
}

public class ShoppingCartManager {
    private static final String CART_FILE = "shopping_cart.dat";
    
    public static void saveCart(ShoppingCart cart) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CART_FILE))) {
            oos.writeObject(cart);
            System.out.println("💾 Cart saved!");
        } catch (IOException e) {
            System.err.println("❌ Failed to save cart: " + e.getMessage());
        }
    }
    
    public static ShoppingCart loadCart() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CART_FILE))) {
            return (ShoppingCart) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("🆕 Creating new cart");
            return new ShoppingCart();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Failed to load cart: " + e.getMessage());
            return new ShoppingCart();
        }
    }
}
```

---

## 🔧 **Section 4: Data Streams (Primitive Types)**

### 📊 Database Record Manager
```java
import java.io.*;

// 👤 Employee record structure
class EmployeeRecord {
    int id;
    String name;
    double salary;
    boolean isActive;
    char department;
    
    public EmployeeRecord(int id, String name, double salary, boolean isActive, char department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.isActive = isActive;
        this.department = department;
    }
}

public class EmployeeDatabase {
    private static final String DB_FILE = "employees.db";
    
    // 📝 Write employee records
    public static void writeEmployees(EmployeeRecord[] employees) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(DB_FILE))) {
            dos.writeInt(employees.length); // Write record count first
            
            for (EmployeeRecord emp : employees) {
                dos.writeInt(emp.id);
                dos.writeUTF(emp.name);
                dos.writeDouble(emp.salary);
                dos.writeBoolean(emp.isActive);
                dos.writeChar(emp.department);
            }
            
            System.out.println("📝 Saved " + employees.length + " employee records");
        }
    }
    
    // 📖 Read employee records
    public static EmployeeRecord[] readEmployees() throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(DB_FILE))) {
            int count = dis.readInt(); // Read record count
            EmployeeRecord[] employees = new EmployeeRecord[count];
            
            for (int i = 0; i < count; i++) {
                int id = dis.readInt();
                String name = dis.readUTF();
                double salary = dis.readDouble();
                boolean isActive = dis.readBoolean();
                char department = dis.readChar();
                
                employees[i] = new EmployeeRecord(id, name, salary, isActive, department);
            }
            
            System.out.println("📖 Loaded " + count + " employee records");
            return employees;
        }
    }
    
    // 📊 Display employee data
    public static void displayEmployees(EmployeeRecord[] employees) {
        System.out.println("\n👥 Employee Database:");
        System.out.println("ID\tName\t\tSalary\t\tActive\tDept");
        System.out.println("---------------------------------------------------");
        
        for (EmployeeRecord emp : employees) {
            System.out.printf("%d\t%-15s\t$%.2f\t%s\t%c%n", 
                emp.id, emp.name, emp.salary, 
                emp.isActive ? "✅" : "❌", emp.department);
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Create sample data
        EmployeeRecord[] employees = {
            new EmployeeRecord(1, "John Doe", 75000.0, true, 'E'),
            new EmployeeRecord(2, "Jane Smith", 82000.0, true, 'M'),
            new EmployeeRecord(3, "Bob Johnson", 68000.0, false, 'S'),
            new EmployeeRecord(4, "Alice Brown", 91000.0, true, 'E')
        };
        
        // Write to file
        writeEmployees(employees);
        
        // Read from file
        EmployeeRecord[] loadedEmployees = readEmployees();
        
        // Display data
        displayEmployees(loadedEmployees);
    }
}
```

### 📈 Stock Market Data Logger
```java
import java.io.*;
import java.util.*;

public class StockDataLogger {
    private static final String STOCK_FILE = "stock_data.bin";
    
    // 📊 Log stock price
    public static void logStockPrice(String symbol, double price, long timestamp, int volume) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(STOCK_FILE, true))) {
            dos.writeUTF(symbol);
            dos.writeDouble(price);
            dos.writeLong(timestamp);
            dos.writeInt(volume);
            System.out.println("📈 Logged: " + symbol + " @ $" + price);
        }
    }
    
    // 📋 Read all stock data
    public static void readStockData() throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(STOCK_FILE))) {
            System.out.println("\n📊 Stock Data History:");
            System.out.println("Symbol\tPrice\t\tTime\t\tVolume");
            System.out.println("-------------------------------------------");
            
            while (dis.available() > 0) {
                String symbol = dis.readUTF();
                double price = dis.readDouble();
                long timestamp = dis.readLong();
                int volume = dis.readInt();
                
                Date date = new Date(timestamp);
                System.out.printf("%s\t$%.2f\t\t%tT\t%d%n", symbol, price, date, volume);
            }
        } catch (EOFException e) {
            // End of file reached
            System.out.println("📋 End of stock data");
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Log some stock prices
        long now = System.currentTimeMillis();
        logStockPrice("AAPL", 150.25, now, 1000000);
        logStockPrice("GOOGL", 2800.50, now + 1000, 750000);
        logStockPrice("MSFT", 300.75, now + 2000, 900000);
        
        // Read all data
        readStockData();
    }
}
```

---

## 🔧 **Section 5: ByteArrayOutputStream**

### 📧 Email Attachment System
```java
import java.io.*;

public class EmailAttachmentSystem {
    
    // 📎 Create email with multiple attachments
    public static void sendEmailWithAttachments(String[] recipients, String subject, String body) throws IOException {
        // Create email content in memory
        ByteArrayOutputStream emailBuffer = new ByteArrayOutputStream();
        
        // Write email headers and body
        emailBuffer.write(("Subject: " + subject + "\n").getBytes());
        emailBuffer.write(("Body: " + body + "\n\n").getBytes());
        emailBuffer.write("--- Attachments ---\n".getBytes());
        
        // Simulate attachment data
        emailBuffer.write("Attachment1: config.txt\n".getBytes());
        emailBuffer.write("Attachment2: report.pdf\n".getBytes());
        
        // Send to multiple recipients
        for (String recipient : recipients) {
            try (FileOutputStream recipientFile = new FileOutputStream(recipient + "_email.txt")) {
                emailBuffer.writeTo(recipientFile);
                System.out.println("📧 Email sent to: " + recipient);
            }
        }
        
        emailBuffer.close();
        System.out.println("✅ Email sent to " + recipients.length + " recipients");
    }
    
    // 📱 SMS notification system
    public static void sendMultipleNotifications(String message) throws IOException {
        ByteArrayOutputStream notification = new ByteArrayOutputStream();
        notification.write(("Notification: " + message + "\n").getBytes());
        notification.write(("Timestamp: " + new Date() + "\n").getBytes());
        
        // Send to multiple channels
        String[] channels = {"sms_queue.txt", "push_queue.txt", "email_queue.txt"};
        
        for (String channel : channels) {
            try (FileOutputStream channelFile = new FileOutputStream(channel)) {
                notification.writeTo(channelFile);
                System.out.println("📱 Notification queued to: " + channel);
            }
        }
        
        notification.close();
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Send email to multiple recipients
        String[] recipients = {"user1", "user2", "user3"};
        sendEmailWithAttachments(recipients, "Monthly Report", "Please find the monthly report attached.");
        
        // Send notifications
        sendMultipleNotifications("System maintenance scheduled for tonight");
    }
}
```

### 💾 Data Backup System
```java
import java.io.*;
import java.util.*;

public class DataBackupSystem {
    
    // 💾 Create backup of important data
    public static void createBackup(Map<String, String> importantData) throws IOException {
        // Create backup in memory first
        ByteArrayOutputStream backupBuffer = new ByteArrayOutputStream();
        
        // Write backup header
        backupBuffer.write(("Backup created: " + new Date() + "\n").getBytes());
        backupBuffer.write("=================================\n".getBytes());
        
        // Write data
        for (Map.Entry<String, String> entry : importantData.entrySet()) {
            String line = entry.getKey() + "=" + entry.getValue() + "\n";
            backupBuffer.write(line.getBytes());
        }
        
        // Create multiple backup copies
        String[] backupLocations = {"backup_primary.txt", "backup_secondary.txt", "backup_offsite.txt"};
        
        for (String location : backupLocations) {
            try (FileOutputStream backupFile = new FileOutputStream(location)) {
                backupBuffer.writeTo(backupFile);
                System.out.println("💾 Backup created: " + location);
            }
        }
        
        backupBuffer.close();
        System.out.println("✅ Backup completed to " + backupLocations.length + " locations");
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Important application data
        Map<String, String> appData = new HashMap<>();
        appData.put("user_count", "10000");
        appData.put("last_login", "2024-01-15");
        appData.put("database_version", "2.1.0");
        appData.put("server_status", "active");
        
        createBackup(appData);
    }
}
```

---

## 🔧 **Section 6: SequenceInputStream**

### 📚 Document Merger
```java
import java.io.*;
import java.util.*;

public class DocumentMerger {
    
    // 📋 Merge multiple documents
    public static void mergeDocuments(String[] sourceFiles, String outputFile) throws IOException {
        List<FileInputStream> inputStreams = new ArrayList<>();
        
        // Create input streams for all source files
        for (String sourceFile : sourceFiles) {
            inputStreams.add(new FileInputStream(sourceFile));
        }
        
        // Create sequence input stream
        SequenceInputStream mergedStream = new SequenceInputStream(Collections.enumeration(inputStreams));
        
        // Write merged content
        try (FileOutputStream output = new FileOutputStream(outputFile);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            
            while ((bytesRead = mergedStream.read(buffer)) != -1) {
                bufferedOutput.write(buffer, 0, bytesRead);
            }
            
            System.out.println("📋 Merged " + sourceFiles.length + " documents into " + outputFile);
        }
        
        mergedStream.close();
    }
    
    // 📊 Create report from multiple sources
    public static void createReport(String[] dataSources, String reportFile) throws IOException {
        // Add header
        try (FileOutputStream headerFile = new FileOutputStream("temp_header.txt")) {
            headerFile.write(("Report Generated: " + new Date() + "\n").getBytes());
            headerFile.write("=====================================\n\n".getBytes());
        }
        
        // Create list of all files (header + data sources)
        List<String> allFiles = new ArrayList<>();
        allFiles.add("temp_header.txt");
        allFiles.addAll(Arrays.asList(dataSources));
        
        // Merge all files
        mergeDocuments(allFiles.toArray(new String[0]), reportFile);
        
        // Clean up temporary file
        new File("temp_header.txt").delete();
        
        System.out.println("📊 Report created: " + reportFile);
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Create sample files
        createSampleFiles();
        
        // Merge documents
        String[] sources = {"section1.txt", "section2.txt", "section3.txt"};
        mergeDocuments(sources, "complete_document.txt");
        
        // Create report
        createReport(sources, "final_report.txt");
    }
    
    // 📝 Helper method to create sample files
    private static void createSampleFiles() throws IOException {
        String[] files = {"section1.txt", "section2.txt", "section3.txt"};
        String[] contents = {
            "Section 1: Introduction\nThis is the introduction section.\n\n",
            "Section 2: Analysis\nThis is the analysis section.\n\n",
            "Section 3: Conclusion\nThis is the conclusion section.\n\n"
        };
        
        for (int i = 0; i < files.length; i++) {
            try (FileWriter writer = new FileWriter(files[i])) {
                writer.write(contents[i]);
            }
        }
        
        System.out.println("📝 Sample files created");
    }
}
```

### 🎵 Audio File Concatenator
```java
import java.io.*;
import java.util.*;

public class AudioFileConcatenator {
    
    // 🎵 Concatenate audio files (simplified example)
    public static void concatenateAudioFiles(String[] audioFiles, String outputFile) throws IOException {
        System.out.println("🎵 Concatenating audio files...");
        
        // Create input streams for all audio files
        List<FileInputStream> audioStreams = new ArrayList<>();
        for (String audioFile : audioFiles) {
            audioStreams.add(new FileInputStream(audioFile));
        }
        
        // Create sequence stream
        SequenceInputStream concatenatedStream = new SequenceInputStream(Collections.enumeration(audioStreams));
        
        // Write concatenated audio
        try (FileOutputStream output = new FileOutputStream(outputFile);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output)) {
            
            byte[] buffer = new byte[4096]; // Larger buffer for audio
            int bytesRead;
            long totalBytes = 0;
            
            while ((bytesRead = concatenatedStream.read(buffer)) != -1) {
                bufferedOutput.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
                
                // Show progress
                if (totalBytes % 1024000 == 0) { // Every MB
                    System.out.print("📊 Processed: " + (totalBytes / 1024000) + " MB\r");
                }
            }
            
            System.out.println("\n🎵 Audio concatenation complete!");
            System.out.println("📏 Total size: " + (totalBytes / 1024000) + " MB");
        }
        
        concatenatedStream.close();
    }
    
    // 🎼 Create playlist from multiple audio files
    public static void createPlaylist(String[] audioFiles, String playlistFile) throws IOException {
        System.out.println("🎼 Creating playlist...");
        
        // First, create a playlist info file
        try (FileWriter playlistInfo = new FileWriter("playlist_info.txt")) {
            playlistInfo.write("Playlist: " + playlistFile + "\n");
            playlistInfo.write("Created: " + new Date() + "\n");
            playlistInfo.write("Tracks: " + audioFiles.length + "\n");
            playlistInfo.write("========================\n");
            
            for (int i = 0; i < audioFiles.length; i++) {
                playlistInfo.write((i + 1) + ". " + audioFiles[i] + "\n");
            }
            playlistInfo.write("========================\n\n");
        }
        
        // Add playlist info to the beginning
        List<String> allFiles = new ArrayList<>();
        allFiles.add("playlist_info.txt");
        allFiles.addAll(Arrays.asList(audioFiles));
        
        // Concatenate all files
        concatenateAudioFiles(allFiles.toArray(new String[0]), playlistFile);
        
        // Clean up
        new File("playlist_info.txt").delete();
        
        System.out.println("🎼 Playlist created: " + playlistFile);
    }
}
```

---

## 🔧 **Section 7: Character Streams (Text Data)**

### 📝 Configuration File Manager
```java
import java.io.*;
import java.util.*;

public class ConfigManager {
    private static final String CONFIG_FILE = "app.config";
    
    // 📝 Write configuration
    public static void writeConfig(Properties config) throws IOException {
        try (FileWriter writer = new FileWriter(CONFIG_FILE);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            
            // Write header
            bufferedWriter.write("# Application Configuration\n");
            bufferedWriter.write("# Generated: " + new Date() + "\n");
            bufferedWriter.write("# ================================\n\n");
            
            // Write properties
            for (String key : config.stringPropertyNames()) {
                bufferedWriter.write(key + "=" + config.getProperty(key) + "\n");
            }
            
            bufferedWriter.flush();
            System.out.println("📝 Configuration saved to " + CONFIG_FILE);
        }
    }
    
    // 📖 Read configuration
    public static Properties readConfig() throws IOException {
        Properties config = new Properties();
        
        try (FileReader reader = new FileReader(CONFIG_FILE);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Skip comments and empty lines
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Parse key=value pairs
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    config.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
            
            System.out.println("📖 Configuration loaded from " + CONFIG_FILE);
        }
        
        return config;
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Create sample configuration
        Properties config = new Properties();
        config.setProperty("database.host", "localhost");
        config.setProperty("database.port", "5432");
        config.setProperty("database.name", "myapp");
        config.setProperty("server.timeout", "30");
        config.setProperty("logging.level", "INFO");
        
        // Write configuration
        writeConfig(config);
        
        // Read configuration
        Properties loadedConfig = readConfig();
        
        // Display configuration
        System.out.println("\n⚙️ Application Configuration:");
        loadedConfig.forEach((key, value) -> 
            System.out.println("  " + key + " = " + value));
    }
}
```

### 📊 CSV Data Processor
```java
import java.io.*;
import java.util.*;

public class CSVProcessor {
    
    // 📊 Process sales data from CSV
    public static void processSalesData(String csvFile) throws IOException {
        System.out.println("📊 Processing sales data from: " + csvFile);
        
        try (FileReader reader = new FileReader(csvFile);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            
            String line;
            boolean isHeader = true;
            double totalSales = 0;
            int recordCount = 0;
            Map<String, Double> salesByProduct = new HashMap<>();
            
            while ((line = bufferedReader.readLine()) != null) {
                if (isHeader) {
                    System.out.println("📋 Header: " + line);
                    isHeader = false;
                    continue;
                }
                
                // Parse CSV line: product,quantity,price,date
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String product = data[0].trim();
                    int quantity = Integer.parseInt(data[1].trim());
                    double price = Double.parseDouble(data[2].trim());
                    
                    double lineTotal = quantity * price;
                    totalSales += lineTotal;
                    recordCount++;
                    
                    salesByProduct.put(product, 
                        salesByProduct.getOrDefault(product, 0.0) + lineTotal);
                }
            }
            
            // Generate report
            generateSalesReport(totalSales, recordCount, salesByProduct);
        }
    }
    
    // 📈 Generate sales report
    private static void generateSalesReport(double totalSales, int recordCount, 
                                          Map<String, Double> salesByProduct) throws IOException {
        
        try (FileWriter writer = new FileWriter("sales_report.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            
            // Write report header
            bufferedWriter.write("Sales Report\n");
            bufferedWriter.write("=============\n");
            bufferedWriter.write("Generated: " + new Date() + "\n\n");
            
            // Summary
            bufferedWriter.write("Summary:\n");
            bufferedWriter.write("--------\n");
            bufferedWriter.write("Total Records: " + recordCount + "\n");
            bufferedWriter.write("Total Sales: $" + String.format("%.2f", totalSales) + "\n");
            bufferedWriter.write("Average Sale: $" + String.format("%.2f", totalSales / recordCount) + "\n\n");
            
            // Sales by product
            bufferedWriter.write("Sales by Product:\n");
            bufferedWriter.write("-----------------\n");
            
            salesByProduct.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    try {
                        bufferedWriter.write(String.format("%-15s: $%.2f%n", 
                            entry.getKey(), entry.getValue()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            
            bufferedWriter.flush();
            System.out.println("📈 Sales report generated: sales_report.txt");
        }
    }
    
    // 📝 Create sample CSV file
    public static void createSampleCSV() throws IOException {
        try (FileWriter writer = new FileWriter("sales_data.csv");
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            
            // Write header
            bufferedWriter.write("Product,Quantity,Price,Date\n");
            
            // Sample data
            String[] sampleData = {
                "Laptop,5,999.99,2024-01-15",
                "Mouse,25,29.99,2024-01-15",
                "Keyboard,15,79.99,2024-01-15",
                "Monitor,8,299.99,2024-01-16",
                "Laptop,3,999.99,2024-01-16",
                "Headphones,12,149.99,2024-01-16"
            };
            
            for (String data : sampleData) {
                bufferedWriter.write(data + "\n");
            }
            
            bufferedWriter.flush();
            System.out.println("📝 Sample CSV created: sales_data.csv");
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Create sample CSV
        createSampleCSV();
        
        // Process the CSV
        processSalesData("sales_data.csv");
    }
}
```

### 📚 Log File Analyzer
```java
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LogAnalyzer {
    
    // 📊 Analyze web server logs
    public static void analyzeWebLogs(String logFile) throws IOException {
        System.out.println("📊 Analyzing web logs: " + logFile);
        
        Map<String, Integer> ipCounts = new HashMap<>();
        Map<String, Integer> statusCounts = new HashMap<>();
        Map<String, Integer> pageCounts = new HashMap<>();
        int totalRequests = 0;
        
        // Pattern for common log format: IP - - [timestamp] "method path protocol" status size
        Pattern logPattern = Pattern.compile(
            "^(\\S+) .* \\[.*\\] \"\\w+ (\\S+) .*\" (\\d+) .*");
        
        try (FileReader reader = new FileReader(logFile);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = logPattern.matcher(line);
                if (matcher.matches()) {
                    String ip = matcher.group(1);
                    String path = matcher.group(2);
                    String status = matcher.group(3);
                    
                    ipCounts.put(ip, ipCounts.getOrDefault(ip, 0) + 1);
                    statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
                    pageCounts.put(path, pageCounts.getOrDefault(path, 0) + 1);
                    totalRequests++;
                }
            }
        }
        
        // Generate analysis report
        generateLogReport(totalRequests, ipCounts, statusCounts, pageCounts);
    }
    
    // 📈 Generate log analysis report
    private static void generateLogReport(int totalRequests, 
                                        Map<String, Integer> ipCounts,
                                        Map<String, Integer> statusCounts,
                                        Map<String, Integer> pageCounts) throws IOException {
        
        try (FileWriter writer = new FileWriter("log_analysis.txt");
             PrintWriter printWriter = new PrintWriter(writer)) {
            
            printWriter.println("Web Log Analysis Report");
            printWriter.println("=======================");
            printWriter.println("Generated: " + new Date());
            printWriter.println("Total Requests: " + totalRequests);
            printWriter.println();
            
            // Top IP addresses
            printWriter.println("Top 10 IP Addresses:");
            printWriter.println("--------------------");
            ipCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> printWriter.printf("%-15s: %d requests%n", 
                    entry.getKey(), entry.getValue()));
            
            printWriter.println();
            
            // Status code distribution
            printWriter.println("Status Code Distribution:");
            printWriter.println("------------------------");
            statusCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> printWriter.printf("%-3s: %d requests%n", 
                    entry.getKey(), entry.getValue()));
            
            printWriter.println();
            
            // Top pages
            printWriter.println("Top 10 Pages:");
            printWriter.println("-------------");
            pageCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> printWriter.printf("%-30s: %d requests%n", 
                    entry.getKey(), entry.getValue()));
            
            printWriter.flush();
            System.out.println("📈 Log analysis report generated: log_analysis.txt");
        }
    }
    
    // 📝 Create sample log file
    public static void createSampleLog() throws IOException {
        try (FileWriter writer = new FileWriter("access.log");
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            
            String[] sampleLogs = {
                "192.168.1.100 - - [15/Jan/2024:10:30:45 +0000] \"GET /index.html HTTP/1.1\" 200 1234",
                "192.168.1.101 - - [15/Jan/2024:10:30:46 +0000] \"GET /about.html HTTP/1.1\" 200 2345",
                "192.168.1.100 - - [15/Jan/2024:10:30:47 +0000] \"GET /contact.html HTTP/1.1\" 200 3456",
                "192.168.1.102 - - [15/Jan/2024:10:30:48 +0000] \"GET /products.html HTTP/1.1\" 200 4567",
                "192.168.1.100 - - [15/Jan/2024:10:30:49 +0000] \"GET /nonexistent.html HTTP/1.1\" 404 567",
                "192.168.1.103 - - [15/Jan/2024:10:30:50 +0000] \"GET /index.html HTTP/1.1\" 200 1234",
                "192.168.1.101 - - [15/Jan/2024:10:30:51 +0000] \"POST /login HTTP/1.1\" 500 123"
            };
            
            for (String log : sampleLogs) {
                bufferedWriter.write(log + "\n");
            }
            
            bufferedWriter.flush();
            System.out.println("📝 Sample log created: access.log");
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        // Create sample log
        createSampleLog();
        
        // Analyze the log
        analyzeWebLogs("access.log");
    }
}
```

---

## 🔧 **Section 8: FileDescriptor**

### 🔄 File Handle Manager
```java
import java.io.*;

public class FileHandleManager {
    
    // 🔄 Demonstrate file descriptor usage
    public static void demonstrateFileDescriptor() throws IOException {
        System.out.println("🔄 Demonstrating FileDescriptor usage...");
        
        File testFile = new File("test_descriptor.txt");
        
        // Create initial file output stream
        try (FileOutputStream fos1 = new FileOutputStream(testFile)) {
            // Get the file descriptor
            FileDescriptor fd = fos1.getFD();
            
            // Write some data
            fos1.write("First write through FOS1\n".getBytes());
            fos1.flush();
            
            // Create another stream using the same file descriptor
            try (FileOutputStream fos2 = new FileOutputStream(fd)) {
                fos2.write("Second write through FOS2 (same descriptor)\n".getBytes());
                fos2.flush();
                
                // Both streams are writing to the same file
                System.out.println("✅ Both streams wrote to the same file");
            }
            
            // Check if file descriptor is valid
            System.out.println("📊 File descriptor valid: " + fd.valid());
        }
        
        // Read the file to verify content
        try (FileReader reader = new FileReader(testFile);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            
            System.out.println("\n📖 File contents:");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("  " + line);
            }
        }
        
        // Clean up
        testFile.delete();
    }
    
    // 🔒 Demonstrate standard file descriptors
    public static void demonstrateStandardDescriptors() throws IOException {
        System.out.println("\n🔒 Standard File Descriptors:");
        
        // Standard input descriptor
        FileDescriptor stdin = FileDescriptor.in;
        System.out.println("📥 Standard Input valid: " + stdin.valid());
        
        // Standard output descriptor
        FileDescriptor stdout = FileDescriptor.out;
        System.out.println("📤 Standard Output valid: " + stdout.valid());
        
        // Standard error descriptor
        FileDescriptor stderr = FileDescriptor.err;
        System.out.println("❌ Standard Error valid: " + stderr.valid());
        
        // Writing to standard output using file descriptor
        try (FileOutputStream stdoutStream = new FileOutputStream(stdout)) {
            stdoutStream.write("🖥️ This is written directly to stdout using FileDescriptor\n".getBytes());
            stdoutStream.flush();
        }
        
        // Writing to standard error using file descriptor
        try (FileOutputStream stderrStream = new FileOutputStream(stderr)) {
            stderrStream.write("⚠️ This is written directly to stderr using FileDescriptor\n".getBytes());
            stderrStream.flush();
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        demonstrateFileDescriptor();
        demonstrateStandardDescriptors();
    }
}
```

---

## 🔧 **Section 9: Advanced Topics**

### 🚀 Buffered Streams for Performance
```java
import java.io.*;
import java.util.concurrent.TimeUnit;

public class BufferedStreamDemo {
    
    // 📊 Compare performance: buffered vs unbuffered
    public static void performanceComparison() throws IOException {
        String testFile = "performance_test.txt";
        String testData = "This is a test line for performance comparison.\n";
        int iterations = 10000;
        
        System.out.println("🚀 Performance Comparison: Buffered vs Unbuffered");
        System.out.println("Writing " + iterations + " lines...\n");
        
        // Test unbuffered writing
        long startTime = System.nanoTime();
        try (FileWriter writer = new FileWriter(testFile)) {
            for (int i = 0; i < iterations; i++) {
                writer.write(testData);
            }
        }
        long unbufferedTime = System.nanoTime() - startTime;
        
        // Test buffered writing
        startTime = System.nanoTime();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testFile))) {
            for (int i = 0; i < iterations; i++) {
                bufferedWriter.write(testData);
            }
        }
        long bufferedTime = System.nanoTime() - startTime;
        
        // Show results
        System.out.println("📊 Results:");
        System.out.println("Unbuffered: " + TimeUnit.NANOSECONDS.toMillis(unbufferedTime) + " ms");
        System.out.println("Buffered:   " + TimeUnit.NANOSECONDS.toMillis(bufferedTime) + " ms");
        System.out.println("Speedup:    " + (unbufferedTime / bufferedTime) + "x faster");
        
        // Clean up
        new File(testFile).delete();
    }
    
    // 📖 Efficient file reading with BufferedReader
    public static void efficientFileReading(String filename) throws IOException {
        System.out.println("\n📖 Efficient file reading with BufferedReader:");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineCount = 0;
            long totalChars = 0;
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                totalChars += line.length();
                
                // Show progress for large files
                if (lineCount % 1000 == 0) {
                    System.out.print("📊 Processed " + lineCount + " lines\r");
                }
            }
            
            System.out.println("\n✅ File processing complete:");
            System.out.println("   Lines: " + lineCount);
            System.out.println("   Characters: " + totalChars);
            System.out.println("   Average line length: " + (totalChars / lineCount));
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        performanceComparison();
        
        // Create a test file for reading demo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("read_test.txt"))) {
            for (int i = 0; i < 5000; i++) {
                writer.write("Line " + i + ": This is a sample line for testing buffered reading.\n");
            }
        }
        
        efficientFileReading("read_test.txt");
        
        // Clean up
        new File("read_test.txt").delete();
    }
}
```

### 🔄 Stream Conversion and Bridges
```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class StreamConversionDemo {
    
    // 🔄 Convert between byte and character streams
    public static void demonstrateStreamConversion() throws IOException {
        System.out.println("🔄 Stream Conversion Demo:");
        
        String testData = "Hello World! 🌍 Unicode characters: ñ, ü, ç";
        
        // Write using byte stream
        try (FileOutputStream fos = new FileOutputStream("conversion_test.txt");
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            
            writer.write(testData);
            System.out.println("📝 Written using: FileOutputStream → OutputStreamWriter → BufferedWriter");
        }
        
        // Read using byte stream converted to character stream
        try (FileInputStream fis = new FileInputStream("conversion_test.txt");
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            
            String line = reader.readLine();
            System.out.println("📖 Read using: FileInputStream → InputStreamReader → BufferedReader");
            System.out.println("📄 Content: " + line);
        }
        
        // Clean up
        new File("conversion_test.txt").delete();
    }
    
    // 🌐 Handle different character encodings
    public static void demonstrateEncodingHandling() throws IOException {
        System.out.println("\n🌐 Character Encoding Demo:");
        
        String multilingual = "English: Hello, Spanish: Hola, French: Bonjour, German: Hallo";
        
        // Write with different encodings
        String[] encodings = {"UTF-8", "ISO-8859-1", "UTF-16"};
        
        for (String encoding : encodings) {
            String filename = "text_" + encoding.replace("-", "_") + ".txt";
            
            try (FileOutputStream fos = new FileOutputStream(filename);
                 OutputStreamWriter writer = new OutputStreamWriter(fos, encoding)) {
                
                writer.write(multilingual);
                System.out.println("📝 Written with " + encoding + " encoding");
            }
            
            // Read back with the same encoding
            try (FileInputStream fis = new FileInputStream(filename);
                 InputStreamReader reader = new InputStreamReader(fis, encoding)) {
                
                StringBuilder content = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1) {
                    content.append((char) character);
                }
                
                System.out.println("📖 Read with " + encoding + ": " + content.toString());
            }
            
            // Clean up
            new File(filename).delete();
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) throws IOException {
        demonstrateStreamConversion();
        demonstrateEncodingHandling();
    }
}
```

---

## 🔧 **Section 10: Best Practices & Performance**

### ✅ Exception Handling Best Practices
```java
import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ExceptionHandlingBestPractices {
    private static final Logger logger = Logger.getLogger(ExceptionHandlingBestPractices.class.getName());
    
    // ✅ Proper exception handling with try-with-resources
    public static void properExceptionHandling(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
            System.out.println("✅ File processed successfully");
            
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, "File not found: " + filename, e);
            System.err.println("❌ File not found: " + filename);
            // Handle missing file gracefully
            createDefaultFile(filename);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO error while processing file: " + filename, e);
            System.err.println("❌ IO error: " + e.getMessage());
            // Implement retry logic or fallback
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error", e);
            System.err.println("❌ Unexpected error: " + e.getMessage());
        }
    }
    
    // 🛡️ Robust file operations with validation
    public static boolean safeFileOperation(String sourceFile, String targetFile) {
        // Validate inputs
        if (sourceFile == null || targetFile == null) {
            System.err.println("❌ Invalid file paths");
            return false;
        }
        
        File source = new File(sourceFile);
        File target = new File(targetFile);
        
        // Check source file
        if (!source.exists()) {
            System.err.println("❌ Source file does not exist: " + sourceFile);
            return false;
        }
        
        if (!source.canRead()) {
            System.err.println("❌ Cannot read source file: " + sourceFile);
            return false;
        }
        
        // Check target directory
        File targetDir = target.getParentFile();
        if (targetDir != null && !targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println("❌ Cannot create target directory: " + targetDir);
                return false;
            }
        }
        
        // Perform the operation
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target))) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            
            System.out.println("✅ File copied successfully: " + sourceFile + " → " + targetFile);
            return true;
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to copy file", e);
            System.err.println("❌ Copy failed: " + e.getMessage());
            
            // Clean up partial file
            if (target.exists()) {
                target.delete();
            }
            
            return false;
        }
    }
    
    // Helper methods
    private static void processLine(String line) {
        // Process line logic here
        System.out.println("Processing: " + line.substring(0, Math.min(50, line.length())));
    }
    
    private static void createDefaultFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Default content created due to missing file\n");
            writer.write("Created at: " + new java.util.Date() + "\n");
            System.out.println("✅ Created default file: " + filename);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create default file", e);
        }
    }
    
    // 🧪 Demo
    public static void main(String[] args) {
        // Test proper exception handling
        properExceptionHandling("nonexistent.txt");
        properExceptionHandling("default_file.txt");
        
        // Test safe file operations
        try (FileWriter writer = new FileWriter("source.txt")) {
            writer.write("This is test content for copying\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        safeFileOperation("source.txt", "backup/target.txt");
        safeFileOperation("nonexistent.txt", "backup/target2.txt");
        
        // Clean up
        new File("source.txt").delete();
        new File("default_file.txt").delete();
        new File("backup/target.txt").delete();
        new File("backup").delete();
    }
}
```

### 🚀 Performance Optimization Techniques
```java
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceOptimization {
    
    // 📊 Benchmark different I/O approaches
    public static void benchmarkIOApproaches() throws IOException {
        String testFile = "benchmark_test.txt";
        byte[] testData = "This is test data for benchmarking