package services.db;

import entities.content.Content;
import entities.content.ContentTypes;
import entities.content.Text;
import entities.content.Video;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import util.QueryExecutor;

import javax.management.Query;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Опредедлиться с хранением текста и уникалной идентификацией
 */
public class ContentService implements Abonent, Runnable {
    private static final Address address = new Address();
    private final QueryExecutor queryExecutor;
    private static final int NUM_OF_CONTENT_ON_PAGE = 10;
    public ContentService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public int addVideo(int userId, String name, String link){
        int videoId;
        if ((videoId = getVideoIdByLink(link)) == -1)
            videoId = addNewVideo(link, name);

        try {
            queryExecutor.execUpdate(String.format("INSERT INTO user_content_video (user_id, video_id, added) VALUES ('%d', '%d', true);", userId, videoId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videoId;
    }

    public List<Video> getAllContentVideos(int userId, int index){
        String query = String.format("SELECT * FROM content_video WHERE index > '%d' AND id NOT IN " +
                        "(SELECT video_id FROM user_content_video WHERE user_id = '%d') LIMIT '%d';",
                index, userId, NUM_OF_CONTENT_ON_PAGE);

        try {
            return queryExecutor.execQuery(query, resultSet -> {
                List<Video> videos = new ArrayList<>();
                Video video;
                while (resultSet.next()){
                    video = new Video();
                    video.setUserId(userId);
                    video.setContentId(resultSet.getInt("id"));
                    video.setLink(resultSet.getString("link"));
                    video.setName(resultSet.getString("name"));
                    video.setRating(resultSet.getInt("rating"));

                    videos.add(video);
                }
                return videos;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Video getVideoById(int userId, int videoId){
        String query = String.format("WITH ucv AS (SELECT video_id, isLiked, isLearned FROM user_content_video WHERE video_id = '%d' AND user_id = '%d') " +
                "SELECT content_video.link, content_video.name, content_video.rating, ucv.isLiked, ucv.isLearned FROM ucv " +
                "INNER JOIN content_video ON content_video.id = ucv.video_id;", videoId, userId);
        try {
            return queryExecutor.execQuery(query, resultSet -> {
                Video video = null;
                if (resultSet.next()){
                    video = new Video();
                    video.setUserId(userId);
                    video.setContentId(videoId);
                    video.setRating(resultSet.getInt("rating"));
                    video.setLink(resultSet.getString("link"));
                    video.setLiked(resultSet.getBoolean("isLiked"));
                    video.setLearned(resultSet.getBoolean("isLearned"));
                    video.setName(resultSet.getString("name"));
                }
                return video;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Video> getLikedVideos(int userId, int index){
        try {
            return queryExecutor.execQuery(String.format("SELECT id, link, name, rating FROM content_video WHERE id IN " +
                    "(SELECT video_id FROM user_content_video WHERE user_id = '%d' AND isLiked = true AND index > '%d') LIMIT '%d';", userId, index, NUM_OF_CONTENT_ON_PAGE), resultSet -> {
                List<Video> videos = new ArrayList<>();
                Video video;
                while (resultSet.next()){
                    video = new Video();
                    video.setUserId(userId);
                    video.setContentId(resultSet.getInt("id"));
                    video.setLink(resultSet.getString("link"));
                    video.setName(resultSet.getString("name"));
                    video.setRating(resultSet.getInt("rating"));

                    videos.add(video);
                }
                return videos;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Video> getLearningVideos(int userId, int index){
        try {
            return queryExecutor.execQuery(String.format("SELECT id, link, name, rating FROM content_video WHERE id IN " +
                    "(SELECT video_id FROM user_content_video WHERE user_id = '%d' AND isLearned = false AND index > '%d') LIMIT '%d';", userId, index, NUM_OF_CONTENT_ON_PAGE), resultSet -> {
                List<Video> videos = new ArrayList<>();
                Video video;
                while (resultSet.next()){
                    video = new Video();
                    video.setUserId(userId);
                    video.setContentId(resultSet.getInt("id"));
                    video.setLink(resultSet.getString("link"));
                    video.setName(resultSet.getString("name"));
                    video.setRating(resultSet.getInt("rating"));

                    videos.add(video);
                }
                return videos;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Video> getLearnedVideos(int userId, int index){
        try {
            return queryExecutor.execQuery(String.format("SELECT id, link, name, rating FROM content_video WHERE id IN " +
                    "(SELECT video_id FROM user_content_video WHERE user_id = '%d' AND isLearned = true AND index > '%d') LIMIT '%d';", userId, index, NUM_OF_CONTENT_ON_PAGE), resultSet -> {
                List<Video> videos = new ArrayList<>();
                Video video;
                while (resultSet.next()){
                    video = new Video();
                    video.setUserId(userId);
                    video.setContentId(resultSet.getInt("id"));
                    video.setLink(resultSet.getString("link"));
                    video.setName(resultSet.getString("name"));
                    video.setRating(resultSet.getInt("rating"));

                    videos.add(video);
                }
                return videos;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Video> getAddedVideos(int userId, int index){
        try {
            return queryExecutor.execQuery(String.format("SELECT id, link, name, rating FROM content_video WHERE id IN " +
                    "(SELECT video_id FROM user_content_video WHERE user_id = '%d' AND added = true AND index > '%d') LIMIT '%d';", userId, index, NUM_OF_CONTENT_ON_PAGE), resultSet -> {
                List<Video> videos = new ArrayList<>();
                Video video;
                while (resultSet.next()){
                    video = new Video();
                    video.setUserId(userId);
                    video.setContentId(resultSet.getInt("id"));
                    video.setLink(resultSet.getString("link"));
                    video.setName(resultSet.getString("name"));
                    video.setRating(resultSet.getInt("rating"));

                    videos.add(video);
                }
                return videos;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void likeVideo(int userId, int videoId){
        String query = String.format("UPDATE user_content_video SET isLiked = true WHERE user_id = '%d' AND video_id = '%d' RETURNING user_id;", userId, videoId);
        int res = 0;
        try {
            res = queryExecutor.execQuery(query, resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("user_id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res == -1) {
            try {
                queryExecutor.execUpdate(String.format("INSERT INTO user_content_video (user_id, video_id, isLiked) VALUES ('%d', '%d', true);", userId, videoId));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveVideoToLearning(int userId, int videoId){
        String query = String.format("UPDATE user_content_video SET isLearned = false WHERE user_id = '%d' AND video_id = '%d' RETURNING user_id;", userId, videoId);
        int res = 0;
        try {
            res = queryExecutor.execQuery(query, resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("user_id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res == -1) {
            try {
                queryExecutor.execUpdate(String.format("INSERT INTO user_content_video (user_id, video_id, isLearned) VALUES ('%d', '%d', false);", userId, videoId));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveVideoToLearned(int userId, int videoId){
        String query = String.format("UPDATE user_content_video SET isLearned = true WHERE user_id = '%d' AND video_id = '%d' RETURNING user_id;", userId, videoId);
        int res = 0;
        try {
            res = queryExecutor.execQuery(query, resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("user_id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res == -1) {
            try {
                queryExecutor.execUpdate(String.format("INSERT INTO user_content_video (user_id, video_id, isLearned) VALUES ('%d', '%d', true);", userId, videoId));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeVideoFromLearning(int userId, int videoId){
        try {
            queryExecutor.execUpdate(String.format("UPDATE user_content_video SET isLearned = null WHERE user_id = '%d' AND video_id = '%d';", userId, videoId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVideoFromLearned(int userId, int videoId){
        try {
            queryExecutor.execUpdate(String.format("UPDATE user_content_video SET isLearned = null WHERE user_id = '%d' AND video_id = '%d';", userId, videoId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeVideoFromAdded(int userId, int videoId){
        try {
            queryExecutor.execUpdate(String.format("UPDATE user_content_video SET added = false WHERE user_id = '%d' AND video_id = '%d';", userId, videoId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getVideoIdByLink(String link){
        try {
            return queryExecutor.execQuery(String.format("SELECT id FROM content_video WHERE link = '%s';", link), resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int addNewVideo(String link, String name){
        try {
            return queryExecutor.execQuery(String.format("INSERT INTO content_video (link, name) VALUES ('%s', '%s') RETURNING id;", link, name), resultSet -> {
                if (resultSet.next())
                    return resultSet.getInt("id");
                return -1;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public static Address getAdr(){
        return address;
    }

    @Override
    public void run() {
        while(true){
            try{
                MessageSystem.INSTANCE.execForService(this);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
