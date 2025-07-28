# HASHIRA ASSIGNMENT

<img width="1007" height="74" alt="Screenshot from 2025-07-28 12-32-00" src="https://github.com/user-attachments/assets/0418208e-53aa-41fe-b630-0037ad7c9989" />



## Nuclear Weapon Analogy

Imagine a nuclear launch code split into secret fragments distributed among trusted officers. Each officer holds an encoded piece of the activation code, and **only when a minimum number (threshold) of officers combine their shares can the full code be recovered**—keeping it secure from misuse or theft.

## Problem

You are given several encoded shares (roots of a secret polynomial), each in different bases. Your task is to decode these shares and reconstruct the **constant term** of the hidden polynomial (the secret) using exactly the minimum number of shares required (`k`).

## Solution Approach

1. **Read and decode shares** from JSON input, converting each encoded value from its given base to an integer.
2. **Use Lagrange interpolation** on the chosen `k` decoded points `(x, y)` to mathematically reconstruct the secret polynomial's constant term, `f(0)`.
3. **Output the reconstructed secret** which is the activation code in the analogy.

This approach ensures the secret is recoverable only by the requisite number of trusted parties working together—simulating secure nuclear launch codes through cryptographic secret sharing.
