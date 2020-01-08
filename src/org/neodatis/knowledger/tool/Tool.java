package org.neodatis.knowledger.tool;

import java.io.BufferedReader;
import java.io.FileReader;

public class Tool {
	static public String getStringFromFile( String in_sFileName )
	{
	 	String sLine = null;
	 	StringBuffer sString = new StringBuffer();
	 	BufferedReader fileReader = null;


	 	// If File is not valid
	 	if( in_sFileName == null )
		{
			return null;
		}
		try
		{
			FileReader f = new FileReader(in_sFileName );
			if( f == null )
			{
				return null;
			}

			fileReader = new BufferedReader( f );

			if( fileReader == null )
			{
				return null;
			}


			while( ( sLine = fileReader.readLine())!= null )
			{
				if(sLine!=null)
				{
					sString.append( sLine).append("\n" );
				}
			}
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				if(fileReader!= null)
				{
					fileReader.close();
				}
			}
			catch(Exception e)
			{
				return null;
			}
		}

		return sString.toString();
	}

}
