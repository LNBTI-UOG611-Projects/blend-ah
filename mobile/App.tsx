import React, {useEffect, useState} from 'react';
import {
  SafeAreaView,
  Text,
  View,
  ActivityIndicator,
  StyleSheet,
} from 'react-native';

function App() {
  const [message, setMessage] = useState('Connecting...');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://10.0.2.2:8080/api/health')
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        return response.text();
      })
      .then(data => setMessage(data))
      .catch(error => setMessage(`Connection failed: ${error.message}`))
      .finally(() => setLoading(false));
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>Blend-Ah</Text>

        {loading ? (
          <ActivityIndicator size="large" />
        ) : (
          <Text style={styles.message}>{message}</Text>
        )}
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F3F0FF',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
  title: {
    fontSize: 32,
    fontWeight: 'bold',
    color: '#3730A3',
    marginBottom: 20,
  },
  message: {
    fontSize: 16,
    color: '#1F2937',
    textAlign: 'center',
  },
});

export default App;