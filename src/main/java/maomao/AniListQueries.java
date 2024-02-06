package maomao;

public class AniListQueries
{
    public String getNewActivities()
    {
        String newActivitiesQuery = """
                query ($userId: Int, $lastActivityTime: Int) {
                    Page {
                        activities (userId: $userId, createdAt_greater: $lastActivityTime) {
                            __typename
                            ... on ListActivity {
                                createdAt
                                status
                                media {
                                    id
                                    type
                                    title {
                                        english
                                    }
                                }
                                user {
                                    name
                                    avatar {
                                        medium
                                    }
                                }
                            }
                        }
                    }
                }
                """;
        
        return sanitizeQuery(newActivitiesQuery);
    }
    
    
    public String getLatestActivityTime()
    {
        String latestActivityTimeQuery = """
                query ($userId: Int) {
                    Activity (userId: $userId, sort: ID_DESC) {
                            __typename
                            ... on ListActivity {
                                createdAt
                        }
                    }
                }
                """;
        
        return sanitizeQuery(latestActivityTimeQuery);
    }
    
    
    public String getUserInfo()
    {
        String userInfoQuery = """
                query ($username: String) {
                    User (search: $username) {
                        id
                        updatedAt
                    }
                }
                """;
        
        return sanitizeQuery(userInfoQuery);
    }
    
    
    private String sanitizeQuery(String query)
    {
        query = query.replace('\n', ' ');
        
        return query;
    }
    
}
