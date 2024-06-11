This application is a comprehensive course management platform designed to facilitate the interaction between students and teachers. The platform provides the following features:

For Students:
Login: Students can securely log in using Spring Security. Passwords are encrypted and not stored in plaintext.
Course Enrollment: Students can enroll in multiple courses while ensuring they do not overlap in schedule.
View Courses and Grades: Students can view their enrolled courses and their respective grades.
Enrollment Restrictions: Students cannot enroll in multiple courses that occur simultaneously or in courses that are already full.

For Teachers:
Login: Teachers can securely log in using Spring Security. Passwords are encrypted and not stored in plaintext.
Course Creation: Teachers can create multiple courses, defining the schedule and maximum number of attendees.
Approve/Deny Enrollments: Teachers can manage student enrollment requests, approving or denying them as needed.
Grading: Teachers can grade students, assigning one grade per course for each student.

Example of course configuration: 

{
	"name": "OOP",
	"description": "Object-oriented programming basics",
	"maxAttendees": 30,
	"schedule": [
		{
			"name": "course",
			"startDate": "11.01.2023",
			"endDate": "01.06.2023",
			"weekDay": "MON",
			"startTime": "10:00",
			"endTime": "12:00"
		},
		{
			"name": "laboratory",
			"startDate": "11.01.2023",
			"endDate": "01.06.2023",
			"weekDay": "TUE",
			"startTime": "13:00",
			"endTime": "15:00"
		}
	]
}
