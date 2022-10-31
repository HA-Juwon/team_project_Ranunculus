package team.ranunculus.entities.board;

import java.util.Date;
import java.util.Objects;

public class BoardEntity {
    public static final String ATTRIBUTE_NAME = "board";
    public static final String ATTRIBUTE_NAME_PLURAL = "boards";

    public static BoardEntity build() {
        return new BoardEntity();
    }

    private int index;
    private String writer;
    private String password;
    private String title;
    private String content;
    private Date createdAt;
    private String emailAdminFlag;

    public BoardEntity() {
    }

    public BoardEntity(int index, String writer, String password, String title, String content, Date createdAt, String emailAdminFlag) {
        this.index = index;
        this.writer = writer;
        this.password = password;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.emailAdminFlag = emailAdminFlag;
    }

    public int getIndex() {
        return index;
    }

    public BoardEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getWriter() {
        return writer;
    }

    public BoardEntity setWriter(String writer) {
        this.writer = writer;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BoardEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BoardEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public BoardEntity setContent(String content) {
        this.content = content;
        return this;
    }



    public Date getCreatedAt() {
        return createdAt;
    }

    public BoardEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getEmailAdminFlag() {
        return emailAdminFlag;
    }

    public BoardEntity setEmailAdminFlag(String emailAdminFlag) {
        this.emailAdminFlag = emailAdminFlag;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEntity that = (BoardEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
