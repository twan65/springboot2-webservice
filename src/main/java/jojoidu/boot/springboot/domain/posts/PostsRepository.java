package jojoidu.boot.springboot.domain.posts;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Postsの基本CRUD生成
 * PostsのEntityクラスの同じパッケージ内で管理する必要がある。
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
