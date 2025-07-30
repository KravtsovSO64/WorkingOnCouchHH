package ru.practicum.android.diploma.domain.models

data class VacancyDetail(
    val id: String,
    val name: String,
    val description: String,
    val salaryFrom: Int,
    val salaryTo: Int,
    val salaryCurrency: String,
    val addressCity: String,
    val addressStreet: String,
    val addressBuilding: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val contactsName: String,
    val contactsEmail: String,
    val contactsPhone: List<String>,
    val employerName: String,
    val employerLogo: String,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry,
) {
    companion object {
        fun empty() = VacancyDetail(
            id = "",
            name = "",
            description = "",
            salaryFrom = 0,
            salaryTo = 0,
            salaryCurrency = "",
            addressCity = "",
            addressStreet = "",
            addressBuilding = "",
            experience = "",
            schedule = "",
            employment = "",
            contactsName = "",
            contactsEmail = "",
            contactsPhone = listOf(),
            employerName = "",
            employerLogo = "",
            area = FilterArea(
                0,
                0,
                "",
                listOf()
            ),
            skills = listOf(),
            url = "",
            industry = FilterIndustry(
                0,
                "",
            ),)
    }
}
