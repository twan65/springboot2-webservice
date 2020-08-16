package jojoidu.boot.springboot.domain.posts;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Postsの基本CRUD生成
 * PostsのEntityクラスの同じパッケージ内で管理する必要がある。
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

    /**
     * Postsテーブルの全てを取得する。
     * ソート：DESC
     *
     * @return PostsList
     */
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
