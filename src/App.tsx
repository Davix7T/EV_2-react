import PrivateRoute from "./components/PrivateRoute";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";

function App(): JSX.Element {
  return (
    <PrivateRoute fallback={<LoginPage />}>
      <HomePage />
    </PrivateRoute>
  );
}

export default App;
