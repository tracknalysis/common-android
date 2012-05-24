/**
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tracknalysis.common.android.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import net.tracknalysis.common.io.SocketManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * @author David Valeri
 */
public class BtSocketManager implements SocketManager {
    
    private static final Logger LOG = LoggerFactory.getLogger(BtSocketManager.class);
    
    private BluetoothDevice device;
    private BluetoothAdapter btAdapter;
    private BtProfile btProfile;
    private volatile BluetoothSocket btSocket;
    
    public enum BtProfile {
        SPP("00001101-0000-1000-8000-00805F9B34FB");
        
        private final UUID uuid;
        
        private BtProfile(String uuid) {
            this.uuid = UUID.fromString(uuid);
        }
        
        public UUID getUuid() {
            return uuid;
        }
    }

    public BtSocketManager(String btAddress, BluetoothAdapter btAdapter, BtProfile btProfile) {
        super();
        this.btAdapter = btAdapter;
        this.btProfile = btProfile;
        // Always returns non-null for valid address.
        device = btAdapter.getRemoteDevice(btAddress);
    }
    
    @Override
    public synchronized void connect() throws IOException {
        if (btSocket == null) {
    
            LOG.debug("BtSocketManager {} connecting...", this);
            try {
                btAdapter.cancelDiscovery();
                            
                btSocket = device.createRfcommSocketToServiceRecord(btProfile.getUuid());
                // Blocking until success
                btSocket.connect();
                
                LOG.info("BtSocketManager {} connected.", this);
            } catch (IOException e) {
                btSocket = null;
                LOG.error("BtSocketManager {} failed to connect.", this);
                throw e;
            }
        } else {
            LOG.debug("BtSocketManager {} has already connected.", this);
        }
    }
    
    @Override
    public synchronized void disconnect() throws IOException {
        if (btSocket != null) {
            btSocket.close();
            btSocket = null;
            LOG.info("BtSocketManager {} disconnected.", this);
        } else {
            LOG.warn("BtSocketManager {} is not connected.", this);
        }
    }
    
    @Override
    public synchronized InputStream getInputStream() throws IOException {
        if (btSocket != null) {
            return btSocket.getInputStream();
        } else {
            return null;
        }
    }

    @Override
    public synchronized OutputStream getOutputStream() throws IOException {
        if (btSocket != null) {
            return btSocket.getOutputStream();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BtSocketManager [device=");
        builder.append(device);
        builder.append(", btAdapter=");
        builder.append(btAdapter);
        builder.append(", btProfile=");
        builder.append(btProfile);
        builder.append(", btSocket=");
        builder.append(btSocket);
        builder.append("]");
        return builder.toString();
    }
}
