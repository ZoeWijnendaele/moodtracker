# Moodtracker

Welcome to my moodtracker, an emotion tracking application designed to help users understand and manage their emotional well-being through visual representation using animal avatars.

## Table of Contents
- [Introduction](#introduction)
- [Target Audience](#target-audience)
- [Main Goal](#main-goal)
- [Functionalities](#functionalities)
- [Installation](#installation)
- [Usage](#usage)
- [About](#about)
- [Future Plans](#future-plans)
- [License](#license)

## Introduction

Our mission is to guide individuals in understanding and mastering their emotional world through the visual representation of animals.  
By providing a simple and intuitive interface, MoodTracker aims to empower users to track their daily moods effectively.


## Target Audience

Moodtracker caters to two primary user groups:

#### People Seeking Stress Reduction or Emotional Management:
- Focus on stress management and understanding mood triggers.
- Require user-friendly interfaces with goal-setting and progress tracking capabilities.

#### Individuals with Mood Disorders:
- Need an easy way to track daily moods and add relevant notes for their treatment.
- Require user-friendly interfaces as they may be in vulnerable states.
- Can benefit from trend analysis and advice to manage mood fluctuations.

## Main Goal

### Functional Requirements:

- **User Registration and Login:**
  Users should be able to create accounts and log in to track their mood.

- **Mood Tracking:**
  Users can input their mood at various times throughout the day with timestamps, with the option to add notes.

- **Mood Entry Overview:**
  Users should be able to view their previously entered moods in a clear list format.

- **Trend Analysis:**
  The app should analyze user mood data and identify trends, such as seasonal patterns.

- **Advice and Recommendations:**
  Based on trend analysis, the app should provide users with advice and recommendations to improve their mood.

- **Notifications:**
  Users should receive notifications about prolonged mood states and suggestions for action.

- **Data Storage:**
  User data, mood entries, and trend data should be securely stored in a database.

- **User Profiles:**
  Users should be able to customize their profiles and potentially share information about themselves.

- **Connect with Professionals:**
  The app should provide users with options to connect with nearby healthcare professionals if needed.

### Non-functional Requirements:

- **Security:**
  User data must be securely stored and communicated, adhering to security standards.

- **Performance:**
  The app should respond quickly and be scalable to support a growing user base.

- **Maintainability:**
  Code should be well-documented for maintenance and updates.

- **User-Friendliness:**
  The user interface should be intuitive and user-friendly.

- **Platform Independence:**
  Consideration for accessibility on multiple platforms (e.g., web and mobile).

- **Privacy:**
  Ensure compliance with privacy laws and protect user information.

- **Scalability:**
  Consider how the app can be scaled to support more users.

## Functionalities

### Work in Progress Features:

- **User Registration and Login:**
  Register using email and password, with optional social media login.
- **User Profiles:**
  Customize profile pages with personal information and avatars.
- **Mood Tracking:**
  Record daily mood with a scale and add notes and timestamps.
- **Mood Entry Overview:**
  View a chronological list of mood entries with filters and search options.
- **Mood Trend Analysis:**
  Display graphs and statistics showing mood changes over time, identifying patterns and trends.
- **Advice based on Trends:**
  Offer suggestions and recommendations based on mood trends for mood improvement.
- **User Notifications:**
  Set reminders for daily mood tracking and send notifications based on trends and analyses.

### Data Stored in the Database:

- **User Data:**
  Email, password, avatar, preferences (e.g., notifications, privacy).
- **Mood Entries:**
  Date, time, mood emotion (via animal avatars), notes, intensity score.
- **Trend Data:**
  Mood statistics, graphs, comparisons with previous periods, machine learning models for future predictions.
- **Social Data (Optional):**
  Friend lists, shared moods and reactions, messages, chat history.
- **Administration Data:**
  User account management, content and moderation management, change tracking logs.

## Installation

To install MoodTracker, please follow these steps:

1. Clone the repository to your local machine:
    ```sh
   https://github.com/ZoeWijnendaele/moodtracker.git
    ```
2. Navigate to the project directory:
    ```sh
    cd moodtrackerdb
    ```

3. Set up the backend using IntelliJ, Maven, MySQL, Spring, Java, JUnit5, Mockito, and H2 according to the provided configuration.

## Usage

To use Moodtracker, follow these guidelines:

1. Register for an account or log in using existing credentials.
2. Customize your profile with personal information and avatars.
3. Track your mood daily using the mood tracking feature, adding notes and timestamps as needed.
4. View your mood entries in the overview section.
5. Analyze mood trends through the agenda.
6. Analyze mood trends through graphs and statistics provided by the app.
7. Receive advice and recommendations based on identified mood trends.
8. Set up notifications for reminders and alerts regarding your mood state.

## About

Moodtracker was created to provide individuals with a simple yet powerful tool to monitor and manage their emotional well-being. By visualizing mood data and providing actionable insights, we aim to empower users on their journey towards better mental health.

## Future Plans

I am committed to continuously improving this Moodtracker. Planned future updates include:

- Finishing the agenda functionalities.
- Enhancing the front-end.
- Integrating AI functionalities.
- Allowing professionals to view users' mood entries.
- Collecting user data for research purposes.

We welcome feedback and suggestions from people to make this MoodTracker even more effective and user-friendly.

## License

This project is licensed under the [MIT License](LICENSE).
