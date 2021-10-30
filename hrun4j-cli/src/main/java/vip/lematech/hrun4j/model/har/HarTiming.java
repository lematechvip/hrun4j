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


/**
 *
 * The time between sending a request and receiving a response is measured in milliseconds
 * @author sangupta/lematech@foxmail.com
 * website https://www.lematech.vip/
 * @version 1.0.1
 */
public class HarTiming {

	public double blocked;

	public double dns;

	public double connect;

	public double send;

	public double wait;

	public double receive;

	public double ssl;

	public String comment;


	@Override
	public String toString() {
		return "HarTiming [blocked=" + blocked + ", dns=" + dns + ", connect=" + connect + ", send=" + send + ", wait=" + wait + ", receive=" + receive + ", ssl=" + ssl + ", comment=" + comment + "]";
	}

}
