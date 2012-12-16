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

import android.os.Handler;
import net.tracknalysis.common.notification.NotificationListener;
import net.tracknalysis.common.notification.NotificationType;

/**
 * An implementation that delegates to a {@link Handler} for distribution of the notifications.
 *
 * @author David Valeri
 */
public class AndroidNotificationListener<T extends NotificationType> implements NotificationListener<T> {

    private final Handler handler;
    
    public AndroidNotificationListener(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void onNotification(T notificationType) {
        handler.obtainMessage(notificationType.getNotificationTypeId(), null).sendToTarget();
    }

    @Override
    public void onNotification(T notificationType, Object messageBody) {
        handler.obtainMessage(notificationType.getNotificationTypeId(), messageBody)
            .sendToTarget();
    }
}
