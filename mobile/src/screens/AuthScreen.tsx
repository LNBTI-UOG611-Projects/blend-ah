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
  ActivityIndicator,
} from 'react-native';
import { login, register } from '../api/auth';

export default function AuthScreen() {
  const [isLogin, setIsLogin] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);

  // Same rules as the backend
  const validate = () => {
    if (!email.trim() || !password) return 'Please enter your email and password.';
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim())) return 'Please enter a valid email.';
    if (!isLogin) {
      if (!fullName.trim()) return 'Please enter your full name.';
      if (password.length < 8) return 'Password must be at least 8 characters.';
      if (password.length > 72) return 'Password must be 72 characters or less.';
      if (password !== confirmPassword) return 'Passwords do not match.';
    }
    return '';
  };

  const switchMode = () => {
    setIsLogin(!isLogin);
    setError('');
    setPassword('');
    setConfirmPassword('');
  };

  const handleAuthentication = async () => {
    const problem = validate();
    if (problem) {
      setError(problem);
      return;
    }
    setError('');
    setLoading(true);
    try {
      const cleanEmail = email.trim().toLowerCase();
      if (isLogin) {
        const user = await login(cleanEmail, password);
        Alert.alert('Welcome back!', `Hi ${user.fullName} 👋`);
        // Later: go to the Dashboard screen here
      } else {
        await register(fullName.trim(), cleanEmail, password);
        Alert.alert('Account created', 'You can now log in.');
        switchMode();
      }
    } catch (e: any) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={styles.container}
    >
      <ScrollView contentContainerStyle={styles.scrollContainer} keyboardShouldPersistTaps="handled">
        {/* Brand Header */}
        <View style={styles.headerContainer}>
          <View style={styles.logoBox}>
            <Text style={styles.logoText}>b</Text>
          </View>
          <Text style={styles.brandTitle}>Blend-ah</Text>
        </View>

        <Text style={styles.welcomeText}>{isLogin ? 'Welcome back!' : 'Create your account'}</Text>
        <Text style={styles.subText}>
          {isLogin ? 'Log in to continue your journey.' : 'One space. Multiple possibilities.'}
        </Text>

        {!isLogin && (
          <>
            <Text style={styles.label}>Full name</Text>
            <TextInput
              style={styles.input}
              placeholder="Your full name"
              placeholderTextColor="#9CA3AF"
              value={fullName}
              onChangeText={setFullName}
            />
          </>
        )}

        <Text style={styles.label}>Email address</Text>
        <TextInput
          style={styles.input}
          placeholder="you@example.com"
          placeholderTextColor="#9CA3AF"
          keyboardType="email-address"
          autoCapitalize="none"
          autoCorrect={false}
          value={email}
          onChangeText={setEmail}
        />

        <Text style={styles.label}>Password</Text>
        <TextInput
          style={styles.input}
          placeholder="••••••••"
          placeholderTextColor="#9CA3AF"
          secureTextEntry
          autoCapitalize="none"
          value={password}
          onChangeText={setPassword}
        />

        {!isLogin && (
          <>
            <Text style={styles.label}>Confirm password</Text>
            <TextInput
              style={styles.input}
              placeholder="••••••••"
              placeholderTextColor="#9CA3AF"
              secureTextEntry
              autoCapitalize="none"
              value={confirmPassword}
              onChangeText={setConfirmPassword}
            />
          </>
        )}

        {isLogin && (
          <View style={styles.rowBetween}>
            <TouchableOpacity style={styles.rememberRow} onPress={() => setRememberMe(!rememberMe)}>
              <View style={[styles.checkbox, rememberMe && styles.checkedBox]} />
              <Text style={styles.rememberText}>Remember me</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => Alert.alert('Coming soon', 'Password reset is not available yet.')}>
              <Text style={styles.linkText}>Forgot password?</Text>
            </TouchableOpacity>
          </View>
        )}

        {!!error && <Text style={styles.errorText}>{error}</Text>}

        <TouchableOpacity
          style={[styles.primaryButton, loading && styles.buttonDisabled]}
          onPress={handleAuthentication}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#FFF" />
          ) : (
            <Text style={styles.primaryButtonText}>{isLogin ? 'Log In' : 'Create Account'}</Text>
          )}
        </TouchableOpacity>

        <View style={styles.dividerContainer}>
          <View style={styles.line} />
          <Text style={styles.orText}>OR</Text>
          <View style={styles.line} />
        </View>

        <TouchableOpacity style={styles.secondaryButton} onPress={switchMode} disabled={loading}>
          <Text style={styles.secondaryButtonText}>
            {isLogin ? 'Create New Account' : 'Already have an account? Log In'}
          </Text>
        </TouchableOpacity>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

// Colours from the Blend-ah design system
const C = {
  primary: '#4F46E5',
  primaryDark: '#3730A3',
  bg: '#F5F3FF',
  surface: '#FFFFFF',
  border: '#E9E7F5',
  text: '#111827',
  text2: '#4B5563',
  muted: '#9CA3AF',
  error: '#DC2626',
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: C.bg },
  scrollContainer: { flexGrow: 1, padding: 24, justifyContent: 'center' },
  headerContainer: { alignItems: 'center', marginBottom: 28 },
  logoBox: {
    width: 68,
    height: 68,
    backgroundColor: C.surface,
    borderRadius: 20,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 10,
    shadowColor: '#4338CA',
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.1,
    shadowRadius: 16,
    elevation: 4,
  },
  logoText: { color: '#4338CA', fontSize: 38, fontWeight: 'bold' },
  brandTitle: { fontSize: 20, fontWeight: 'bold', color: C.primaryDark },
  welcomeText: { fontSize: 26, fontWeight: '700', color: C.text },
  subText: { fontSize: 14, color: C.text2, marginTop: 4, marginBottom: 8 },
  label: { fontSize: 13, fontWeight: '600', color: '#374151', marginBottom: 7, marginTop: 16 },
  input: {
    height: 50,
    backgroundColor: C.surface,
    borderRadius: 14,
    paddingHorizontal: 16,
    fontSize: 15,
    color: C.text,
    borderWidth: 1.5,
    borderColor: C.border,
  },
  rowBetween: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 16 },
  rememberRow: { flexDirection: 'row', alignItems: 'center' },
  checkbox: { width: 18, height: 18, borderRadius: 6, borderWidth: 2, borderColor: C.primary, marginRight: 8 },
  checkedBox: { backgroundColor: C.primary },
  rememberText: { fontSize: 13, color: '#374151' },
  linkText: { fontSize: 13, color: C.primary, fontWeight: '600' },
  errorText: { color: C.error, fontSize: 13, marginTop: 14 },
  primaryButton: {
    height: 52,
    backgroundColor: C.primary,
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 24,
    shadowColor: C.primary,
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.3,
    shadowRadius: 16,
    elevation: 6,
  },
  buttonDisabled: { opacity: 0.6 },
  primaryButtonText: { color: '#FFF', fontSize: 15, fontWeight: '600' },
  dividerContainer: { flexDirection: 'row', alignItems: 'center', marginVertical: 18 },
  line: { flex: 1, height: 1, backgroundColor: C.border },
  orText: { marginHorizontal: 12, color: C.muted, fontSize: 12, fontWeight: '600' },
  secondaryButton: {
    height: 52,
    borderWidth: 1.5,
    borderColor: C.primary,
    borderRadius: 16,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: C.surface,
  },
  secondaryButtonText: { color: C.primaryDark, fontSize: 15, fontWeight: '600' },
});