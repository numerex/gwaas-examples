package com.numerex.ota;

/*
 *   
 *
 * Copyright  1990-2007 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 */

import java.io.*;

/**
 * This class encapsulates dynamic linear byte array that is handled
 * as stream. This class is a superclass for <code>Packet</code> that
 * encapsulates JDWP packet. <code>ByteBuffer</code> class contains
 * all the generic (not JDWP specific) methods to work with JDWP packet-kind
 * structures.
 *
 * @see jdwp.Packet
 * @see jdwp.Command
 * @see jdwp.Reply
 */
class ByteBuffer {
    
    /**
     * The size that buffer increases if necessary
     */
    private int Delta;
    
    /**
     * The current size of filled part of the buffer
     */
    private int CurrentSize;
    
    /**
     * The index of the byte that will be read next
     */
    int parseOffset;
    
    /**
     * The contents of the buffer
     */
    protected byte[] bytes;

    /**
     * Constructs a new <code>ByteBuffer</code> object with default
     * initial size and delta.
     *
     * @see ByteBuffer#Delta
     */
    public ByteBuffer() {
		this(128, 128);
    }
    
    /**
     * Constructs a new <code>ByteBuffer</code> object with specified
     * initial size and delta.
     *
     * @param InitialSize initial size of the buffer
     * @param Delta the size that buffer increases if necessary
     *
     * @see ByteBuffer#Delta
     */
    public ByteBuffer(int InitialSize, int Delta) {
	    if (Delta <= 0)
	    	Delta = 16;
		this.Delta = Delta;
		CurrentSize = InitialSize;
		bytes = new byte[InitialSize];
		parseOffset = 0;
    }

    public ByteBuffer(byte[] incoming){
    	
    	this.Delta = 128;
    	CurrentSize = incoming.length;
    	bytes = incoming;
    	parseOffset = 0;    	
    }
    
    
    /**
     * Returns length of the filled part of the buffer
     *
     * @return length of the filled part of the buffer
     */
    public int length() {
    	return CurrentSize;
    }

    /**
     * Checks if the buffer is large enough to place the
     * specified number of bytes. If no, the buffer is
     * automatically increased to necessary size.
     *
     * @param Space a number of bytes that it's necessary to place
     */
    private void checkSpace(int Space) {
		if (bytes.length >= CurrentSize + Space) 
			return;

		int NewSize = bytes.length;
		while (NewSize < CurrentSize + Space)
			NewSize = NewSize + Delta;

		byte[] NewData = new byte[NewSize];

		for (int i = 0; i < CurrentSize; i++)
			NewData[i] = bytes[i];

		bytes = NewData;
    }

    /**
     * Stores byte at the specified index in the buffer. If specified index
     * is outside the filled area of the buffer <code>BoundException</code>
     * is raised.
     *
     * @param off index where byte should be placed
     * @param b byte that should be placed
     * @throws BoundException if specified index is outside the filled area of
     * the buffer
     */
    public void putByte(int off, int b) throws Exception {
    	if ((off < 0) || (off > CurrentSize))
    		throw new Exception();

        bytes[off] = (byte) (b & 0xFF);
        position(++off);
    }

    /**
     * Stores a number of bytes at the specified location of the buffer. If
     * at least one byte is to be stored outside the already filled area of the
     * buffer <code>BoundException</code> is raised.
     *
     * @param off index in the buffer where the first byte should be stored
     * @param b array of bytes whose values should be stored in the buffer
     * @param start the first index in the array of bytes that should be stored
     * @param len the number of bytes that should be stored in the buffer
     *
     * @throws BoundException if at least one byte should be stored outside the
     * already filled area of the buffer
     */
    public void putBytes(int off, byte[] b, int start, int len) throws Exception {
        for (int i = 0; i < len; i++)
			putByte(off++, b[start++]);
    }

    /**
     * Stores an integer value (short, int, long) in the buffer. The value is
     * stored in Java (little endian) format. If at least one byte of the value
     * is to be placed outside the alrady filled area of the buffer
     * <code>BoundException</code> is raised
     *
     * @param off index where the first byte of the value should be stored
     * @param l a value that should be stored
     * @param count length of the value (2 for short, 4 for int, 8 for long)
     *
     * @throws BoundException if at least one byte of the value should be
     * stored outside the already filled area of the buffer
     */
    public void putID(int off, long l, int count) throws Exception {

		if ((count <= 0) || (count > 8))
			throw new Exception();
	
		int shift = (count - 1) * 8;

		for (int i = 0; i < count; i++) {
			putByte(off++, (int) ((l >>> shift) & 0xFF));
			shift = shift - 8;
        }
    }

    /**
     * Stores <code>int</code> value in the buffer starting from specified
     * index. The value is stored in little endian format.
     * If at least one byte of the value
     * is to be placed outside the alrady filled area of the buffer
     * <code>Exception</code> is raised.
     *
     * @param off index where the first byte of the value should be stored
     * @param b the value that should be stored
     * @throws Exception if at least one byte of the value should be
     * stored outside the already filled area of the buffer
     */
    public void putInt(int off, int b) throws Exception {
		putID(off, b, 4);		
    }
    
    /**
     * Stores <code>short</code> value in the buffer starting from specified
     * index. The value is stored in little endian format.
     * If at least one byte of the value
     * is to be placed outside the alrady filled area of the buffer
     * <code>Exception</code> is raised.
     *
     * @param off index where the first byte of the value should be stored
     * @param b the value that should be stored
     * @throws Exception if at least one byte of the value should be
     * stored outside the already filled area of the buffer
     */
    public void putShort(int off, int b) throws Exception {
		putID(off, b, 2);		
    }

    /**
     * Stores <code>long</code> value in the buffer starting from specified
     * index. The value is stored in little endian format.
     * If at least one byte of the value
     * is to be placed outside the alrady filled area of the buffer
     * <code>Exception</code> is raised.
     *
     * @param off index where the first byte of the value should be stored
     * @param b the value that should be stored
     * @throws Exception if at least one byte of the value should be
     * stored outside the already filled area of the buffer
     */
    public void putLong(int off, long l) throws Exception {
		putID(off, l, 8);
    }

    /**
     * Adds byte to the end of the filled part of the buffer.
     * If there is no place to
     * store this data the buffer is automatically increased.
     *
     * @param b a byte to be appended
     */
    public void addByte(int b) {
    	checkSpace(1);

    	int where = CurrentSize;
        CurrentSize++;

    	try {
	        putByte(where, b);
	    }
	    catch (Exception e) {};
    }

    /**
     * Adds a number of bytes to the end of the filled part of the buffer.
     * If there is no place to
     * store this data the buffer is automatically increased.
     *
     * @param b a byte array the data should be appended from
     * @param start the index in the byte array of the first byte that
     * should be appended
     * @param len a number of bytes that should be added to the buffer
     */
    public void addBytes(byte[] b, int start, int len) {
    	checkSpace(len);

    	int where = CurrentSize;
        CurrentSize = CurrentSize + len;

    	try {
	        putBytes(where, b, start, len);
	    }
	    catch (Exception e) {};
    }

    /**
     * Adds an integer value (int, short, long)
     * to the end of the filled part of the buffer.
     * If there is no place to
     * store this data the buffer is automatically increased.
     * The value is stored in the little endian format.
     *
     * @param l a value to be appended
     * @param count a size of the value in bytes (2 for <code>short</code>,
     * 4 for <code>int</code>, 8 for <code>long</code>)
     */
    public void addID(long l, int count) {
        checkSpace(count);

    	int where = CurrentSize;
        CurrentSize = CurrentSize + count;

    	try {
	        putID(where, l, count);
	    }
	    catch (Exception e) {};
    }

    /**
     * Adds <code>int</code> to the end of the filled part of the buffer.
     * If there is no place to
     * store this data the buffer is automatically increased.
     * The value is stored in little endian format.
     *
     * @param b a value to be appended
     */
    public void addInt(int b) {
		addID(b, 4);		
    }

    /**
     * Adds <code>short</code> to the end of the filled part of the buffer.
     * If there is no place to
     * store this data the buffer is automatically increased.
     * The value is stored in little endian format.
     *
     * @param b a value to be appended
     */
    public void addShort(int b) {
		addID(b, 2);		
    }

    
    
    
    /**
     * Adds <code>long</code> to the end of the filled part of the buffer.
     * If there is no place to
     * store this data the buffer is automatically increased.
     * The value is stored in little endian format.
     *
     * @param b a value to be appended
     */
    public void addLong(long l) {
		addID(l, 8);
    }

    /**
     * Adds <code>String</code> to the end of the filled part of the buffer.
     * If there is no place to
     * store this data the buffer is automatically increased.
     * The value is stored in UTF-8 format.
     *
     * @param s a string to be appended
     */
    public void addString(String s) {                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                try{
                    dos.writeUTF(s);
                }catch(IOException e ){
                    System.err.println("Error creating the UTF-8 sring");
                    return;
                }
                byte[] buf = baos.toByteArray();
                int len = ((buf[0] << 8) & 0xFF00) | (buf[1] & 0xFF);
                addByte(0);
                addByte(0);
		addBytes(buf, 0, len + 2);
    }

    /**
     * Moves the reading marker to the beginning of the buffer.
     * Typically the process of reading data from the buffer is
     * organized as follows: at first move the reading marker to the
     * some start position (for example, in the beginning of the buffer)
     * and then consequently read the data using get methods (for example,
     * <code>getInt()</code>). So the process of reading data from the byte
     * buffer is looks like reading data from generic stream (for example,
     * from file).
     */
    public void resetParser() {
		parseOffset = 0;
    }

    /**
     * Move the reading marker to the specified position.
     *
     * @param i index of the buffer where reading marker should point
     */
    public void resetParser(int i) {
		parseOffset = i;
    }

    /**
     * Checks if reading marker points to the end of filled data of the
     * buffer. This method is analogue of <code>eof</code> method for the
     * files.
     *
     * @return <code>true</code> if reading marker points to the end of
     * filled data, <code>false</code> otherwise
     */
    public boolean isParsed() {
    	return (parseOffset == CurrentSize);
    }

    /**
     * Tries to read next byte from the buffer. Byte is to read is one
     * that is pointed by reading marker. After completing the operation
     * the reading marker is incremented.
     *
     * @return current byte from the buffer
     * @throws Exception if byte to be read is outside the filled area
     */
    public int getByte() throws Exception {
    	
    	//if (parseOffset > CurrentSize)
    	//	throw new Exception("ParseOffset greater than currentsize " + parseOffset + " " + CurrentSize);

		return (int) (bytes[parseOffset++] & 0xFF);
    }
    
    /**
     * Tries to read next integer value (short, int, long) from the buffer.
     * Value is read is one
     * that is pointed by reading marker. After completing the operation
     * the reading marker is incremented. The value is stored in little endian
     * format.
     *
     * @param count a size in bytes of integer value (2 for <code>short</code>,
     * 4 for <code>int</code>, 8 for <code>long</code>)
     * @return current integer value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */
    public long getID(int count) throws Exception {

		if ((count <= 0) || (count > 8))
			throw new Exception();

		long l = 0;
		for (int i = 0; i < count; i++)
			l = (l * 0x100) + getByte();
		return l;
    }

    /**
     * Tries to read next <code>int</code> from the buffer.
     * Value is read is one
     * that is pointed by reading marker. After completing the operation
     * the reading marker is incremented. The value is stored in little endian
     * format.
     *     
     * @return current <code>int</code> value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */
    public int getInt() throws Exception {
		return (int) getID(4);
    }

    /**
     * Tries to read next <code>short</code> from the buffer.
     * Value is read is one
     * that is pointed by reading marker. After completing the operation
     * the reading marker is incremented. The value is stored in little endian
     * format.
     *     
     * @return current <code>short</code> value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */    
    public int getShort() throws Exception {
		return (int) getID(2);
    }

    /**
     * Tries to read next <code>long</code> from the buffer.
     * Value is read is one
     * that is pointed by reading marker. After completing the operation
     * the reading marker is incremented. The value is stored in little endian
     * format.
     *     
     * @return current <code>long</code> value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */    
    public long getLong() throws Exception {
		return getID(8);
    }

    /**
     * Tries to read next string from the buffer.
     * Value is read is one
     * that is pointed by reading marker. After completing the operation
     * the reading marker is incremented. The value is stored in UTF-8
     * format.
     *     
     * @return current UTF-8 string from the buffer
     * @throws Exception if value to be read is outside the filled area
     */    
    public String getString() throws Exception {
		int l = getInt();
		if (l < 0)
    		throw new Exception();
                if (l == 0)
                    return "";

                byte[] d = new byte[l+2];
                d[0] = (byte)((l >> 8) & 0xFF);
                d[1] = (byte)(l & 0xFF);
		for (int i = 0; i < l; i++){                        
                    d[i+2] = (byte) getByte();                        
                }
                
                ByteArrayInputStream bais = new ByteArrayInputStream(d);
                DataInputStream dis = new DataInputStream(bais);
                
                try{
                    String res = dis.readUTF();
                    return res;
                }catch(IOException e){
                    throw new Exception(e.getMessage());
                }
    }

    /**
     * Tries to read a single byte from the buffer. Byte is to read is
     * located at specified index.
     *
     * @param off the index of the buffer where the required byte is located
     * @return a required byte from the buffer
     * @throws Exception if byte to be read is outside the filled area
     */
    public int getByte(int off) throws Exception {
        int old_offset = parseOffset;
        parseOffset = off;

        int r = getByte();
        parseOffset = old_offset;
        return r;
    }
    
    /**
     * Tries to read an integer value (short, int, long) from the buffer.
     * Value is to read is located at specified index. The value is stored in
     * little endian format.
     *
     * @param off the index of the buffer where the required value is located
     * @param count a size in bytes of integer value (2 for <code>short</code>,
     * 4 for <code>int</code>, 8 for <code>long</code>)
     * @return a required value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */
    public long getID(int off, int count) throws Exception {
        int old_offset = parseOffset;
        parseOffset = off;

        long l = getID(count);
        parseOffset = old_offset;
        return l;
    }

    /**
     * Tries to read an <code>int</code> value from the buffer.
     * Value is to read is located at specified index. The value is stored in
     * little endian format.
     *
     * @param off the index of the buffer where the required value is located     
     * @return a required value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */
    public int getInt(int off) throws Exception {
	return (int) getID(off, 4);
    }

    /**
     * Tries to read an <code>short</code> value from the buffer.
     * Value is to read is located at specified index. The value is stored in
     * little endian format.
     *
     * @param off the index of the buffer where the required value is located     
     * @return a required value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */
    public int getShort(int off) throws Exception {
	return (int) getID(off, 2);
    }

    /**
     * Tries to read an <code>long</code> value from the buffer.
     * Value is to read is located at specified index. The value is stored in
     * little endian format.
     *
     * @param off the index of the buffer where the required value is located     
     * @return a required value from the buffer
     * @throws Exception if value to be read is outside the filled area
     */
    public long getLong(int off) throws Exception {
	return getID(off, 8);
    }

    /**
     * Tries to read a string from the buffer.
     * The string is to read is located at specified index. The value is stored
     * in UTF-8 format.
     *
     * @param off the index of the buffer where the required string is located     
     * @return a required string from the buffer
     * @throws Exception if string to be read is outside the filled area
     */
    public String getString(int off) throws Exception {

        int old_offset = parseOffset;
        parseOffset = off;

        String s = getString();

        parseOffset = old_offset;

        return s;
    }

    /**
     * Deletes a few bytes from the beginning of the buffer and copies the
     * last part to the beginning of the buffer.
     *
     * @param count a number of bytes to be deleted from the head of the buffer
     */
    public void deleteBytes(int count) {
        int j = 0;
        while (count < CurrentSize)
        	bytes[j++] = bytes[count++];
        CurrentSize = j;
    }

    /**
     * Clears the buffer by setting the size of the filled area to zero.
     * The reading marker's position is not affected by this method so if
     * the reading marker was not positioned to the beginning of the buffer
     * it'll become to be in invalid position.
     */
    public void resetBuffer() {
        CurrentSize = 0;
    }

    /**
     * Returns string representation of the contents of the buffer from
     * the specified index. This method is invoked by KJDB when JDWP reply
     * packet is not received (usually it's a fatal error)and this information
     * is useful for localizing the problem.
     *
     * @param start the first index that should be printed
     * @return string representation of a part of the byte buffer
     *
     * @see jdwp.BackEndTest#printReplies
     */
    public String toString(int start) {

	String Result = "", DisplayLine = "";

        for (int i = start; i < length(); i++) {

			//HexLine = HexLine + Tools.Hex(bytes[i], 2) + " ";

			if (bytes[i] >= 0 && bytes[i] < 32)
				DisplayLine = DisplayLine + ".";
			else
				DisplayLine = DisplayLine + new String(bytes, i, 1);

			/*
			if ((i == length() - 1) || (((i - start) & 0x0F) == 0x0F)) {
				Result = Result +
						 Tools.Hex(j, 4) + ": " +
						 Tools.PadR(HexLine, 48) + "  " +
						 DisplayLine + "\n";
				HexLine = "";
				DisplayLine = "";
				j = j + 16;
			}
*/
        }
		return Result;
    }

    /**
     * Returns string representation of the object. Currently this method is
     * not used by KJDB but it may be useful for debugging purposes.
     *
     * @return string representation of the object
     */
    public String toString() {
		return toString(0);
    }

	public void get(byte[] obj_data) throws Exception {
		
		for (int i=0; i<obj_data.length; i++)
			obj_data[i] = (byte) getByte();
	}

	public static ByteBuffer allocate(int i) {
		
		return new ByteBuffer(i, 128);
	}

	public void position(int i) {
		this.resetParser(i);
	}
	public int position() {
		// TODO Auto-generated method stub
		return parseOffset;
	}

	public void put(byte objectid) throws Exception {
		this.putByte(position(), objectid);
		
	}

	public byte[] array() {
		// TODO Auto-generated method stub
		return this.bytes;
	}

	public static ByteBuffer wrap(byte[] incoming) {
		// TODO Auto-generated method stub
		//  public ByteBuffer(int InitialSize, int Delta) {
			ByteBuffer	byteBuffer = new ByteBuffer(incoming);
			return byteBuffer;
		  
				
	}

	public void putInt(int i) throws Exception {
		
		this.putInt(position(), i);
	}
	
	public void putLong(long time) throws Exception {
		// TODO Auto-generated method stub
		this.putLong(position(), time);
		
	}

	public int capacity() {
		// TODO Auto-generated method stub
		return this.length();
	}

	public int get() throws Exception {
		
			return this.getByte();
		
	}

	public void addBytes(byte[] data) {
		
		addBytes(data,0, data.length);
	}

	public void putShort(short size) throws Exception {
		this.putShort(position(), (int)size);
		
	}

	public void putFloat(float data) throws Exception {
		
		this.putInt(position(), Float.floatToIntBits(data));
	}
	
	
	
	
	
	
	public float getFloat() throws Exception {
		return (float) Float.intBitsToFloat(getInt());
		
	}
	
	public void putDouble(double d) throws Exception {
		this.putLong(Double.doubleToLongBits(d));
	}
	
	
	public double getDouble() throws Exception {
		return Double.longBitsToDouble(this.getLong());
	}
	
	
	
	
}

