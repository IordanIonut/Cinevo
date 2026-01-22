package com.cinovo.backend.Utils;

public class RetryUtil
{
    public static void retry(Runnable action)
    {
        int attempts = 0;
        while(true)
        {
            try
            {
                action.run();
                return;
            }
            catch(Exception e)
            {
                if(++attempts >= 3)
                    throw e;
                try
                {
                    Thread.sleep(50L * attempts);
                }
                catch(InterruptedException ignored)
                {
                }
            }
        }
    }
}