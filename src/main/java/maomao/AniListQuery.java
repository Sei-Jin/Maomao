package maomao;

public class AniListQuery
{
    public String getActivityQuery()
    {
        String activityQuery = """
                query ($search: Int) {
                    Activity (userId: $search, sort: ID_DESC) {
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
                                    large
                                    medium
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
