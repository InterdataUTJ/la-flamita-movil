package com.interdata.laflamita.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.interdata.laflamita.view.theme.LaFlamitaTheme

@Composable
fun Pedido(
    modifier: Modifier = Modifier,
    id: String = "0",
    fecha: String = "00/00/0000",
    total: String = "00.00",
    onPedidoClick: (String) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.onSecondary),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Button(
            onClick = { onPedidoClick(id) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "#$id",
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = fecha,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "$$total MXN",
            fontWeight = FontWeight.ExtraBold,
        )
        
    }
}

@Composable
@Preview(device = "id:pixel_5", showBackground = true)
fun PedidoPreview() {
    LaFlamitaTheme {
        Pedido()
    }
}