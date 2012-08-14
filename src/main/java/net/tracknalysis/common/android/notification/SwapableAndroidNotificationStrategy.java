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
package net.tracknalysis.common.android.notification;

import net.tracknalysis.common.notification.NotificationStrategy;
import net.tracknalysis.common.notification.NotificationType;
import android.os.Handler;

/**
 * A strategy that lets users swap the handler out dynamically.  Useful for detaching and attaching
 * to a notification source that outlives the lifecycle of the creator of the source.
 *
 * @author David Valeri
 */
public class SwapableAndroidNotificationStrategy<T extends NotificationType> implements NotificationStrategy<T> {
    private volatile Handler handler;
    
    public SwapableAndroidNotificationStrategy(Handler handler) {
        super();
        this.handler = handler;
    }
    
    @Override
    public synchronized void sendNotification(
            net.tracknalysis.common.notification.NotificationType notificationType) {
        if (handler != null) {
            handler.obtainMessage(notificationType.getNotificationTypeId(), null).sendToTarget();
        }
    }
    
    @Override
    public synchronized void sendNotification(
            net.tracknalysis.common.notification.NotificationType notificationType, Object messageBody) {
        if (handler != null) {
            handler.obtainMessage(notificationType.getNotificationTypeId(),
                    messageBody).sendToTarget();
        }
    }

    public synchronized void setHandler(Handler handler) {
        this.handler = handler;
    }
}