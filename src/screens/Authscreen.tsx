import React, { useState } from 'react';
import {
  StyleSheet,
  Text,
  View,
  TextInput,
  TouchableOpacity,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from 'react-native';

// Update this URL based on your testing environment:
// Android Emulator: 'http://10.0.2.2:8080/api/auth'
// iOS Simulator: 'http://localhost:8080/api/auth'
const API_BASE_URL = 'http://10.0.2.2:8080/api/auth';

export default function AuthScreen({ navigation }: { navigation?: any }) {
  const [isLogin, setIsLogin] = useState(true);

  // Form states
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [fullName, setFullName] = useState('');
  const [username, setUsername] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);

  const handleAuthentication = async () => {
    try {
      const endpoint = isLogin ? `${API_BASE_URL}/login` : `${API_BASE_URL}/register`;
      const payload = isLogin
        ? { email, password }
        : { fullName, username, email, password };

      if (!isLogin && password !== confirmPassword) {
        Alert.alert('Error', 'Passwords do not match');
        return;
      }

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      const data = await response.json();

      if (response.ok) {
        Alert.alert(
          'Success',
          isLogin ? 'Welcome back to Blend-ah!' : 'Account created successfully!'
        );
        if (isLogin) {
          // Navigate to your Dashboard/App Space screen here
          // navigation.replace('Dashboard');
        } else {
          setIsLogin(true); // Switch back to login view after successful registration
        }
      } else {
        Alert.alert('Authentication Failed', data.error || 'Something went wrong');
      }
    } catch (error) {
      Alert.alert('Network Error', 'Could not connect to Spring Boot backend server.');
    }
  };

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={styles.container}
    >
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        {/* Brand Header */}
        <View style={styles.headerContainer}>
          <View style={styles.logoBox}>
            <Text style={styles.logoText}>b</Text>
          </View>
          <Text style={styles.brandTitle}>Blend-ah</Text>
          <Text style={styles.welcomeText}>
            {isLogin ? 'Welcome Back!' : 'Create Your Account'}
          </Text>
          <Text style={styles.subText}>
            {isLogin ? 'Log in to continue your journey.' : 'One space. Multiple possibilities.'}
          </Text>
        </View>

        {/* Form Fields Card */}
        <View style={styles.card}>
          {!isLogin && (
            <>
              <Text style={styles.label}>Full Name</Text>
              <TextInput
                style={styles.input}
                placeholder="Full Name"
                placeholderTextColor="#A0A0A0"
                value={fullName}
                onChangeText={setFullName}
              />

              <Text style={styles.label}>Username</Text>
              <TextInput
                style={styles.input}
                placeholder="@Username"
                placeholderTextColor="#A0A0A0"
                value={username}
                onChangeText={setUsername}
              />
            </>
          )}

          <Text style={styles.label}>Email address</Text>
          <TextInput
            style={styles.input}
            placeholder="you@example.com"
            placeholderTextColor="#A0A0A0"
            keyboardType="email-address"
            autoCapitalize="none"
            value={email}
            onChangeText={setEmail}
          />

          <Text style={styles.label}>Password</Text>
          <TextInput
            style={styles.input}
            placeholder="••••••••"
            placeholderTextColor="#A0A0A0"
            secureTextEntry
            value={password}
            onChangeText={setPassword}
          />

          {!isLogin && (
            <>
              <Text style={styles.label}>Confirm Password</Text>
              <TextInput
                style={styles.input}
                placeholder="••••••••"
                placeholderTextColor="#A0A0A0"
                secureTextEntry
                value={confirmPassword}
                onChangeText={setConfirmPassword}
              />
            </>
          )}

          {isLogin && (
            <View style={styles.rowBetween}>
              <TouchableOpacity
                style={styles.rememberRow}
                onPress={() => setRememberMe(!rememberMe)}
              >
                <View style={[styles.checkbox, rememberMe && styles.checkedBox]} />
                <Text style={styles.rememberText}>Remember Me</Text>
              </TouchableOpacity>
              <TouchableOpacity>
                <Text style={styles.linkText}>Forgot Password?</Text>
              </TouchableOpacity>
            </View>
          )}

          {/* Primary Action Button */}
          <TouchableOpacity style={styles.primaryButton} onPress={handleAuthentication}>
            <Text style={styles.primaryButtonText}>
              {isLogin ? 'Log In' : 'Create New Account'}
            </Text>
          </TouchableOpacity>

          <View style={styles.dividerContainer}>
            <View style={styles.line} />
            <Text style={styles.orText}>OR</Text>
            <View style={styles.line} />
          </View>

          {/* Toggle Button */}
          <TouchableOpacity
            style={styles.secondaryButton}
            onPress={() => setIsLogin(!isLogin)}
          >
            <Text style={styles.secondaryButtonText}>
              {isLogin ? 'Create New Account' : 'Already have an account? Log In'}
            </Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8F9FE' },
  scrollContainer: { flexGrow: 1, padding: 24, justifyContent: 'center' },
  headerContainer: { alignItems: 'center', marginBottom: 24 },
  logoBox: {
    width: 56,
    height: 56,
    backgroundColor: '#5C40CC',
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 12,
  },
  logoText: { color: '#FFF', fontSize: 32, fontWeight: 'bold' },
  brandTitle: { fontSize: 24, fontWeight: 'bold', color: '#1A1A1A', marginBottom: 4 },
  welcomeText: { fontSize: 20, fontWeight: '600', color: '#333', marginBottom: 4 },
  subText: { fontSize: 14, color: '#666' },
  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 24,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 3,
  },
  label: { fontSize: 13, fontWeight: '600', color: '#444', marginBottom: 6, marginTop: 12 },
  input: {
    backgroundColor: '#F5F6FA',
    borderRadius: 12,
    paddingHorizontal: 16,
    paddingVertical: 12,
    fontSize: 15,
    color: '#333',
    borderWidth: 1,
    borderColor: '#EFEFF4',
  },
  rowBetween: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 16,
  },
  rememberRow: { flexDirection: 'row', alignItems: 'center' },
  checkbox: { width: 18, height: 18, borderRadius: 4, borderWidth: 2, borderColor: '#5C40CC', marginRight: 8 },
  checkedBox: { backgroundColor: '#5C40CC' },
  rememberText: { fontSize: 13, color: '#444' },
  linkText: { fontSize: 13, color: '#5C40CC', fontWeight: '600' },
  primaryButton: {
    backgroundColor: '#5C40CC',
    borderRadius: 14,
    paddingVertical: 16,
    alignItems: 'center',
    marginTop: 24,
  },
  primaryButtonText: { color: '#FFF', fontSize: 16, fontWeight: 'bold' },
  dividerContainer: { flexDirection: 'row', alignItems: 'center', marginVertical: 20 },
  line: { flex: 1, height: 1, backgroundColor: '#EFEFF4' },
  orText: { marginHorizontal: 10, color: '#888', fontSize: 12, fontWeight: '600' },
  secondaryButton: {
    borderWidth: 1.5,
    borderColor: '#5C40CC',
    borderRadius: 14,
    paddingVertical: 14,
    alignItems: 'center',
  },
  secondaryButtonText: { color: '#5C40CC', fontSize: 15, fontWeight: '600' },
});