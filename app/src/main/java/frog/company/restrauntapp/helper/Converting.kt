package frog.company.restrauntapp.helper


class Converting {
    fun onConvertDouble(text: Double): String {
        return text.toString().replace(",", ".")
    }
}