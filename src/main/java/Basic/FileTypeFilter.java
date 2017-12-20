/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Ben
 */
public class FileTypeFilter implements FileFilter{
    private String extension;
    
    public FileTypeFilter(String extension)
    {
        this.extension = extension;
    }
    
    @Override
    public boolean accept(File pathname) {
        String filePath = pathname.getName();
        if(filePath.endsWith(extension))
            return true;
        else
            return false;
    }
    
}
