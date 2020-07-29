package me.sujanpoudel.mvidemo.data.typeConverter

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

object OffsetDateTimeConverter {
    private val formatter = ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let { return formatter.parse(value, OffsetDateTime::from) }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}