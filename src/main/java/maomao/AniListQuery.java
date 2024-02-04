package maomao;

public class AniListQuery
{
    public String getActivityQuery()
    {
        String activityQuery = """
                query ($userId: Int, $lastActivityTime: Int) {
                    Page {
                        activities (userId: $userId, createdAt_greater: $lastActivityTime, sort: ID_DESC) {
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
        
        return sanitizeQuery(activityQuery);
    }
    
    
    private String sanitizeQuery(String query)
    {
        query = query.replace('\n', ' ');
        
        return query;
    }
    
}
