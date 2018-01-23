/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.model;

import java.util.Date;

public interface Game {
    int getId();

    int getTeam1Id();

    String getTeam1Name();

    String getTeam1Icon();

    int getTeam2Id();

    String getTeam2Name();

    String getTeam2Icon();

    Date getTime();

    int getTeam1Score();

    int getTeam2Score();

    int getGameWeek();

    boolean getIsFinished();

    Date getLastEdited();

    String getWeekdayFormatted();

    String getDateFormatted();

    String getTimeFormatted();

}
