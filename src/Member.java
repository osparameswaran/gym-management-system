public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String membershipType;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private String status;
    
    public Member() {}
    
    public Member(String name, String email, String phone, String membershipType, 
                 java.time.LocalDate startDate, java.time.LocalDate endDate, String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipType = membershipType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    
    public java.time.LocalDate getStartDate() { return startDate; }
    public void setStartDate(java.time.LocalDate startDate) { this.startDate = startDate; }
    
    public java.time.LocalDate getEndDate() { return endDate; }
    public void setEndDate(java.time.LocalDate endDate) { this.endDate = endDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}