package team.ranunculus.entities.board;

import java.util.Date;
import java.util.Objects;

public class QnaEntity {
    public static final String ATTRIBUTE_NAME = "boardQna";
    public static final String ATTRIBUTE_NAME_PLURAL = "boardQna";

    public static QnaEntity build() {
        return new QnaEntity();
    }

    private int index;
    private String writer;
    private String password;
    private String title;
    private String content;
    private Date createdAt = new Date();
    private String emailAdminFlag;

    public QnaEntity() {
    }

    public QnaEntity(int index, String writer, String password, String title, String content, Date createdAt, String emailAdminFlag) {
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

    public QnaEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getWriter() {
        return writer;
    }

    public QnaEntity setWriter(String writer) {
        this.writer = writer;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public QnaEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public QnaEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public QnaEntity setContent(String content) {
        this.content = content;
        return this;
    }



    public Date getCreatedAt() {
        return createdAt;
    }

    public QnaEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getEmailAdminFlag() {
        return emailAdminFlag;
    }

    public QnaEntity setEmailAdminFlag(String emailAdminFlag) {
        this.emailAdminFlag = emailAdminFlag;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QnaEntity that = (QnaEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
