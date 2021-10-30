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

import lombok.Data;

import java.util.List;

/**
 * HAR response information
 * @author sangupta/lematech@foxmail.com
 * website https://www.lematech.vip/
 * @version 1.0.1
 */

@Data
public class HarResponse {

    private int status;

    private String statusText;

    private String httpVersion;

    private List<HarHeader> headers;

    private List<HarCookie> cookies;

    private HarContent content;

    private String redirectURL;

    private long headersSize;

    private long bodySize;

    @Override
    public String toString() {
        return "HTTP " + this.status + " (" + this.statusText + ")";
    }

}
