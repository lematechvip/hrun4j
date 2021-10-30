/**
 * har - HAR file reader, writer and viewer
 * Copyright (c) 2014-2016, Sandeep Gupta
 * <p>
 * http://sangupta.com/projects/har
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vip.lematech.hrun4j.model.har;

import com.sangupta.jerry.util.UriUtils;
import lombok.Data;

import java.util.Objects;

/**
 * An array containing all requests, each item of the array is an object composed of the data of a request, sorted according to StartedDateTime
 * @author sangupta/lematech@foxmail.com
 * website https://www.lematech.vip/
 * @version 1.0.1
 */

@Data
public class HarEntry implements Comparable<HarEntry> {

    private String pageref;

    private String startedDateTime;

    private double time;

    private HarRequest request;

    private HarResponse response;

    private HarCache cache;

    private HarTiming timings;

    private String serverIPAddress;

    private String connection;

    private String comment;

    @Override
    public String toString() {
        return request.getMethod() + " " + UriUtils.extractPath(request.getUrl());
    }

    @Override
    public int compareTo(HarEntry o) {
        if (Objects.isNull(o)) {
            return -1;
        }
        return this.startedDateTime.compareTo(o.startedDateTime);
    }

}
