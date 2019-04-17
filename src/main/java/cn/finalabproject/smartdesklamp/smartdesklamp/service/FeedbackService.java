package cn.finalabproject.smartdesklamp.smartdesklamp.service;

import cn.finalabproject.smartdesklamp.smartdesklamp.model.Feedback;
import lombok.Setter;

import java.util.Date;

public interface FeedbackService {
    Feedback getFeedBack(int uid, Date startDate,Date endDate);
}
