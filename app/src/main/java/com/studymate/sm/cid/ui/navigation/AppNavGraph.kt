package com.studymate.sm.cid.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.studymate.sm.cid.ui.screens.AcademicCalendarScreen
import com.studymate.sm.cid.ui.screens.CategoryScreen
import com.studymate.sm.cid.ui.screens.DashboardScreen
import com.studymate.sm.cid.ui.screens.DeveloperInfoScreen
import com.studymate.sm.cid.ui.screens.ExamsScreen
import com.studymate.sm.cid.ui.screens.FinanceScreen
import com.studymate.sm.cid.ui.screens.NotesScreen
import com.studymate.sm.cid.ui.screens.ScheduleScreen
import com.studymate.sm.cid.ui.screens.SettingsScreen
import com.studymate.sm.cid.ui.screens.StudyTargetsScreen
import com.studymate.sm.cid.ui.screens.SubjectDetailScreen
import com.studymate.sm.cid.ui.screens.SubjectEditScreen
import com.studymate.sm.cid.ui.screens.SubjectListScreen
import com.studymate.sm.cid.ui.screens.TasksScreen
import com.studymate.sm.cid.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {
    val viewModel: AppViewModel = viewModel()
    val navController: NavHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    fun openDrawer() = scope.launch { drawerState.open() }
    fun closeDrawer() = scope.launch { drawerState.close() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.padding(20.dp)) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Study Mate", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Teman Belajar Digital", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                }
                drawerMenuItems.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            closeDrawer()
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.Category.route) {
                CategoryScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.SubjectList.route) {
                SubjectListScreen(
                    viewModel,
                    onMenuClick = { openDrawer() },
                    onSubjectClick = { id -> navController.navigate(Screen.SubjectDetail.createRoute(id)) },
                    onAddClick = { navController.navigate(Screen.SubjectAdd.route) }
                )
            }
            composable(Screen.SubjectAdd.route) {
                SubjectEditScreen(viewModel, subjectId = null, onBack = { navController.popBackStack() })
            }
            composable(
                Screen.SubjectDetail.route,
                arguments = listOf(navArgument("subjectId") { type = NavType.LongType })
            ) { entry ->
                val subjectId = entry.arguments?.getLong("subjectId") ?: 0L
                SubjectDetailScreen(
                    viewModel,
                    subjectId = subjectId,
                    onBack = { navController.popBackStack() },
                    onEditClick = { navController.navigate(Screen.SubjectEdit.createRoute(subjectId)) }
                )
            }
            composable(
                Screen.SubjectEdit.route,
                arguments = listOf(navArgument("subjectId") { type = NavType.LongType })
            ) { entry ->
                val subjectId = entry.arguments?.getLong("subjectId") ?: 0L
                SubjectEditScreen(viewModel, subjectId = subjectId, onBack = { navController.popBackStack() })
            }
            composable(Screen.Tasks.route) {
                TasksScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.Finance.route) {
                FinanceScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.Exams.route) {
                ExamsScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.DeveloperInfo.route) {
                DeveloperInfoScreen(onMenuClick = { openDrawer() })
            }
            composable(Screen.Schedule.route) {
                ScheduleScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.Notes.route) {
                NotesScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.AcademicCalendar.route) {
                AcademicCalendarScreen(viewModel, onMenuClick = { openDrawer() })
            }
            composable(Screen.StudyTargets.route) {
                StudyTargetsScreen(viewModel, onMenuClick = { openDrawer() })
            }
        }
    }
}
