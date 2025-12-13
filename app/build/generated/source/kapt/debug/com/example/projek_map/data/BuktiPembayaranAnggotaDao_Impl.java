package com.example.projek_map.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BuktiPembayaranAnggotaDao_Impl implements BuktiPembayaranAnggotaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BuktiPembayaranAnggota> __insertionAdapterOfBuktiPembayaranAnggota;

  public BuktiPembayaranAnggotaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBuktiPembayaranAnggota = new EntityInsertionAdapter<BuktiPembayaranAnggota>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bukti_pembayaran_anggota` (`id`,`kodePegawai`,`uri`,`tanggal`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BuktiPembayaranAnggota entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getKodePegawai() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getKodePegawai());
        }
        if (entity.getUri() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getUri());
        }
        if (entity.getTanggal() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTanggal());
        }
      }
    };
  }

  @Override
  public Object insertBukti(final BuktiPembayaranAnggota item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBuktiPembayaranAnggota.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBuktiForUser(final String kode,
      final Continuation<? super List<BuktiPembayaranAnggota>> $completion) {
    final String _sql = "SELECT * FROM bukti_pembayaran_anggota WHERE kodePegawai = ? ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BuktiPembayaranAnggota>>() {
      @Override
      @NonNull
      public List<BuktiPembayaranAnggota> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final List<BuktiPembayaranAnggota> _result = new ArrayList<BuktiPembayaranAnggota>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BuktiPembayaranAnggota _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final String _tmpUri;
            if (_cursor.isNull(_cursorIndexOfUri)) {
              _tmpUri = null;
            } else {
              _tmpUri = _cursor.getString(_cursorIndexOfUri);
            }
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            _item = new BuktiPembayaranAnggota(_tmpId,_tmpKodePegawai,_tmpUri,_tmpTanggal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMaxId(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(id) FROM bukti_pembayaran_anggota";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
