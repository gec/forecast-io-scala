package com.film42.forecastioapi.model

import spray.json._
import com.eclipsesource.json._
import java.util.Date

import scala.util.Try

sealed trait DT { def datetime: Date }

case class Alert(title: String, time: Int, expires: Int, description: String, uri: String)
  extends DT { def datetime = new Date(time * 1000L) }
case class Alerts(alerts: Array[Alert])

case class MinuteDataPoint(time: Int, precipIntensity: Double, precipProbability: Double)
  extends DT { def datetime = new Date(time * 1000L) }
case class Minutely(summary: String, icon: String, data: Array[MinuteDataPoint])

class Flags(json: JsonObject) {
  private def asStringArray(v: JsonValue) =
    v.asArray.values.toArray.map(x => x.toString)

  def sources: Array[String] = asStringArray( json.get("sources") )
  def station(source: String): Array[String] = {
    try asStringArray( json.get(s"$source-stations") )
    catch { case e: Exception => Array() }
  }
  def units: String = json.get("units").asString
}

case class CurrentDataPoint(
  time: Int,
  summary: String ,
  icon: String ,
  nearestStormDistance: Option[Double],
  nearestStormBearing: Option[Double],
  precipIntensity: Double,
  precipProbability: Double,
  temperature: Double,
  apparentTemperature: Double,
  dewPoint: Double,
  humidity: Double,
  windSpeed: Double,
  windBearing: Double,
  visibility: Option[Double],
  cloudCover: Double,
  pressure: Option[Double],
  ozone: Option[Double]) extends DT { def datetime = new Date(time * 1000L) }

case class HourDataPoint(
  time: Int,
  summary: String ,
  icon: String ,
  precipIntensity: Double,
  precipProbability: Double,
  temperature: Double,
  apparentTemperature: Double,
  dewPoint: Option[Double],
  humidity: Option[Double],
  windSpeed: Option[Double],
  windBearing: Option[Double],
  visibility: Option[Double],
  cloudCover: Option[Double],
  pressure: Option[Double],
  ozone: Option[Double]) extends DT { def datetime = new Date(time * 1000L) }

case class Hourly(
  summary: String,
  icon: String,
  data: Array[HourDataPoint])


/*
  Why is this so ugly? Because case class limits of 22, that's why!
 */
class Daily(json: JsonObject) {
  def summary: String =
    try json.get("summary").asString
    catch { case e: Exception => "" }
  def icon: String  =
    try json.get("icon").asString
    catch { case e: Exception => "" }
  def data: Array[DayDataPoint] = {
    val data = json.get("data").asArray.values.toArray
    data.map(x => new DayDataPoint(x.asInstanceOf[JsonValue].asObject))
  }
}

class DayDataPoint(json: JsonObject) extends DT {
  def time: Int = json.get("time").asInt
  def datetime = new Date(time * 1000L)
  def summary: String  = json.get("summary").asString
  def icon: String  = json.get("icon").asString
  def sunriseTime: Int = json.get("sunriseTime").asInt
  def sunsetTime: Int = json.get("sunsetTime").asInt
  def moonPhase: Double = json.get("moonPhase").asDouble
  def precipIntensity: Double = json.get("precipIntensity").asDouble
  def precipIntensityMax: Double = json.get("precipIntensityMax").asDouble
  def precipProbability: Double = json.get("precipProbability").asDouble
  def temperatureMin: Double = json.get("temperatureMin").asDouble
  def temperatureMinTime: Int = json.get("temperatureMinTime").asInt
  def temperatureMinDateTime: Date = new Date(temperatureMinTime * 1000L)
  def temperatureMax: Double = json.get("temperatureMax").asDouble
  def temperatureMaxTime: Int = json.get("temperatureMaxTime").asInt
  def temperatureMaxDateTime: Date = new Date(temperatureMaxTime * 1000L)
  def apparentTemperatureMin: Double = json.get("apparentTemperatureMin").asDouble
  def apparentTemperatureMinTime: Int = json.get("apparentTemperatureMinTime").asInt
  def apparentTemperatureMinDateTime: Date = new Date(apparentTemperatureMinTime * 1000L)
  def apparentTemperatureMax: Double = json.get("apparentTemperatureMax").asDouble
  def apparentTemperatureMaxTime: Int = json.get("apparentTemperatureMaxTime").asInt
  def apparentTemperatureMaxDateTime: Date = new Date(apparentTemperatureMaxTime * 1000L)
  def dewPoint: Option[Double] = Try(json.get("dewPoint").asDouble).toOption
  def humidity: Option[Double] = Try(json.get("humidity").asDouble).toOption
  def windSpeed: Option[Double] = Try(json.get("windSpeed").asDouble).toOption
  def windBearing: Option[Double] = Try(json.get("windBearing").asDouble).toOption
  def visibility: Option[Double] = Try(json.get("visibility").asDouble).toOption
  def cloudCover: Option[Double] = Try(json.get("cloudCover").asDouble).toOption
  def pressure: Option[Double] = Try(json.get("pressure").asDouble).toOption
  def ozone: Option[Double] = Try(json.get("ozone").asDouble).toOption
}

object ForecastJsonProtocol extends DefaultJsonProtocol {
  implicit val currentDataPointFormat = jsonFormat17(CurrentDataPoint)
  implicit val hourDataPointFormat = jsonFormat15(HourDataPoint)
  implicit val hourlyDataFormat = jsonFormat3(Hourly)
  implicit val alertDataFormat = jsonFormat5(Alert)
  implicit val minuteDataFormat = jsonFormat3(MinuteDataPoint)
  implicit val minutelyFormat = jsonFormat3(Minutely)

  // Root is an Array
  implicit object AlertsApiResultsFormat extends RootJsonFormat[Alerts] {
    def read(value: JsValue) = Alerts(value.convertTo[Array[Alert]])
    def write(obj: Alerts) = obj.alerts.toJson
  }
}