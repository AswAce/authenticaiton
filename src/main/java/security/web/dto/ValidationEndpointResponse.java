package security.web.dto;

public class ValidationEndpointResponse {
    private boolean active;
    private String name;
    private String email;
    private String imageUrl;

    public ValidationEndpointResponse(boolean active) {
        this.active = active;
    }

    public ValidationEndpointResponse(boolean active, String name, String email, String imageUrl) {
        this(active);
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
