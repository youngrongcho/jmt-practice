package CoffeeOrderWebApp.practice.stamp;

import CoffeeOrderWebApp.practice.auditing.TimeManager;
import CoffeeOrderWebApp.practice.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Stamp extends TimeManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stampId;

    @Column(name = "count")
    private int stampCount=0;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(member.getStamp()!=this){
            member.setStamp(this);
        }
    }
}
